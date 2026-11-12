package com.example.campuspass.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// The Figma design is a single light theme, so dynamic colour and dark mode are switched off
// to keep the app looking the same on every device.
private val CampusPassColors = lightColorScheme(
    primary = Navy,
    onPrimary = White,
    secondary = Navy,
    onSecondary = White,
    background = White,
    onBackground = TextPrimary,
    surface = White,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = BorderGrey
)

@Composable
fun CampusPassTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CampusPassColors,
        typography = Typography,
        content = content
    )
}
