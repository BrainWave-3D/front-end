package com.example.brainwave3d.ui.presentation.auth_screen.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainwave3d.common.Resource
import com.example.brainwave3d.data.remote.dto.auth.SignupRequest
import com.example.brainwave3d.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.FullNameChange -> {
                _state.update {
                    it.copy(fullName = event.fullName)
                }
            }

            is SignUpUiEvent.EmailChange -> {
                _state.update {
                    it.copy(email = event.email)
                }
            }

            is SignUpUiEvent.PasswordChange -> {
                _state.update {
                    it.copy(
                        password = event.password,
                        passwordsMatch = event.password == it.confirmPassword || it.confirmPassword.isEmpty()
                    )
                }
            }

            is SignUpUiEvent.ConfirmPasswordChange -> {
                _state.update {
                    it.copy(
                        confirmPassword = event.confirmPassword,
                        passwordsMatch = event.confirmPassword == it.password
                    )
                }
            }

            is SignUpUiEvent.TogglePasswordVisibility -> {
                _state.update {
                    it.copy(isPasswordVisible = !it.isPasswordVisible)
                }
            }

            is SignUpUiEvent.ToggleConfirmPasswordVisibility -> {
                _state.update {
                    it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
                }
            }

            is SignUpUiEvent.SignUp -> {
                signUp()
            }

            is SignUpUiEvent.ClearError -> {
                _state.update {
                    it.copy(error = "")
                }
            }
        }
    }

    private fun signUp() {
        // Check if passwords match
        if (_state.value.password != _state.value.confirmPassword) {
            _state.update {
                it.copy(
                    passwordsMatch = false,
                    error = "Passwords do not match"
                )
            }
            return
        }

        // Check if all fields are filled
        if (_state.value.fullName.isBlank() ||
            _state.value.email.isBlank() ||
            _state.value.password.isBlank()) {
            _state.update {
                it.copy(error = "Please fill in all fields")
            }
            return
        }

        viewModelScope.launch {
            val signupRequest = SignupRequest(
                fullName = _state.value.fullName.trim(),
                email = _state.value.email.trim(),
                password = _state.value.password
            )

            authRepository.signup(signupRequest).collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                error = "",
                                signupResponse = null
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "",
                                signupResponse = response.data
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = response.message ?: "An unexpected error occurred",
                                signupResponse = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun resetState() {
        _state.update { SignUpUiState() }
    }
}
