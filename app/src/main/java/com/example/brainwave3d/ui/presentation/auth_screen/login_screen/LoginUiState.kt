package com.example.brainwave3d.ui.presentation.auth_screen.login_screen

import com.example.brainwave3d.data.remote.dto.auth.AuthResponse

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val loginResponse: AuthResponse? = null,
    val isPasswordVisible: Boolean = false
)