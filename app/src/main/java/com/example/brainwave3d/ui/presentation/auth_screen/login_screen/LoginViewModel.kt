package com.example.brainwave3d.ui.presentation.auth_screen.login_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainwave3d.common.Resource
import com.example.brainwave3d.data.remote.dto.auth.LoginRequest
import com.example.brainwave3d.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChange -> {
                _state.update {
                    it.copy(email = event.email)
                }
            }

            is LoginUiEvent.PasswordChange -> {
                _state.update {
                    it.copy(password = event.password)
                }
            }

            is LoginUiEvent.TogglePasswordVisibility -> {
                _state.update {
                    it.copy(isPasswordVisible = !it.isPasswordVisible)
                }
            }

            is LoginUiEvent.Login -> {
                login()
            }

            is LoginUiEvent.ForgotPassword -> {
                // Handle forgot password logic
            }

            is LoginUiEvent.ClearError -> {
                _state.update {
                    it.copy(error = "")
                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            val loginRequest = LoginRequest(
                email = _state.value.email.trim(),
                password = _state.value.password
            )

            authRepository.login(loginRequest).collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                error = "",
                                loginResponse = null
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "",
                                loginResponse = response.data
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = response.message ?: "An unexpected error occurred",
                                loginResponse = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun resetState() {
        _state.update { LoginUiState() }
    }
}
