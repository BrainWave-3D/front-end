package com.example.brainwave3d.ui.presentation.auth_screen.signup_screen

import com.example.brainwave3d.data.remote.dto.auth.AuthResponse

data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val signupResponse: AuthResponse? = null,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val passwordsMatch: Boolean = true
)