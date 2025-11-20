package com.example.brainwave3d.ui.presentation.auth_screen.signup_screen

sealed class SignUpUiEvent {
    data class FullNameChange(val fullName: String) : SignUpUiEvent()
    data class EmailChange(val email: String) : SignUpUiEvent()
    data class PasswordChange(val password: String) : SignUpUiEvent()
    data class ConfirmPasswordChange(val confirmPassword: String) : SignUpUiEvent()
    data object TogglePasswordVisibility : SignUpUiEvent()
    data object ToggleConfirmPasswordVisibility : SignUpUiEvent()
    data object SignUp : SignUpUiEvent()
    data object ClearError : SignUpUiEvent()
}