package com.example.campuspass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.campuspass.ui.navigation.CampusPassApp
import com.example.campuspass.ui.theme.CampusPassTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusPassTheme {
                CampusPassApp()
            }
        }
    }
}
