package com.example.campuspass

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.campuspass.ui.login.LoginRoute
import com.example.campuspass.ui.theme.CampusPassTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusPassTheme {
                // For now the app opens on Login. Navigation to the student screens is added next.
                LoginRoute(
                    onLoginSuccess = {
                        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
