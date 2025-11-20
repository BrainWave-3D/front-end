package com.example.brainwave3d.ui.presentation.auth_screen.login_screen

sealed class LoginUiEvent {
    data class EmailChange(val email: String) : LoginUiEvent()
    data class PasswordChange(val password: String) : LoginUiEvent()
    data object TogglePasswordVisibility : LoginUiEvent()
    data object Login : LoginUiEvent()
    data object ForgotPassword : LoginUiEvent()
    data object ClearError : LoginUiEvent()
}