package com.example.campuspass.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Everything the login screen needs to draw itself. */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

/**
 * Holds the login form state and validation, so the screen itself only draws.
 * The real sign-in call (Firebase Auth) will be plugged into [onLoginClick] by the backend work.
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value.trim(), errorMessage = null) }

    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value, errorMessage = null) }

    /** Checks the form and calls [onSuccess] if it is valid. */
    fun onLoginClick(onSuccess: () -> Unit) {
        val state = _uiState.value
        val error = validate(state.email, state.password)
        if (error != null) {
            _uiState.update { it.copy(errorMessage = error) }
            return
        }
        // TODO: replace with Firebase Auth sign-in and role lookup (student or club)
        onSuccess()
    }

    companion object {
        /** Returns an error message, or null when the details are acceptable. */
        fun validate(email: String, password: String): String? = when {
            email.isBlank() || password.isBlank() -> "Enter your email and password."
            !email.endsWith("@nottingham.edu.my", ignoreCase = true) -> "Use your university email (@nottingham.edu.my)."
            password.length < 6 -> "Password must be at least 6 characters."
            else -> null
        }
    }
}
