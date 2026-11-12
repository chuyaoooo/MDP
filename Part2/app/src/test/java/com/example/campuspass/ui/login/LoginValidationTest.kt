package com.example.campuspass.ui.login

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LoginValidationTest {

    @Test
    fun emptyFields_showMissingDetailsError() {
        assertEquals("Enter your email and password.", LoginViewModel.validate("", ""))
    }

    @Test
    fun nonUniversityEmail_isRejected() {
        assertEquals(
            "Use your university email (@nottingham.edu.my).",
            LoginViewModel.validate("someone@gmail.com", "secret123")
        )
    }

    @Test
    fun shortPassword_isRejected() {
        assertEquals(
            "Password must be at least 6 characters.",
            LoginViewModel.validate("hcyka1@nottingham.edu.my", "123")
        )
    }

    @Test
    fun validUniversityDetails_pass() {
        assertNull(LoginViewModel.validate("hcyka1@nottingham.edu.my", "secret123"))
    }

    @Test
    fun emailCheck_ignoresCapitalLetters() {
        assertNull(LoginViewModel.validate("Student@Nottingham.edu.my", "secret123"))
    }
}
