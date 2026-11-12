package com.example.campuspass.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

// Figma uses Roboto (the Android default) with a 1.35 line height on most text
private fun style(size: Int, weight: FontWeight, lineHeight: TextUnit = (size * 1.35).sp) = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight,
    letterSpacing = 0.sp,
    color = TextPrimary
)

val Typography = Typography(
    // "CampusPass" on the login screen
    headlineLarge = style(30, FontWeight.SemiBold),
    // Screen headings such as "Login" and "Discover events"
    headlineSmall = style(22, FontWeight.SemiBold),
    // App bar titles
    titleLarge = style(20, FontWeight.SemiBold),
    // Event titles in detail views
    titleMedium = style(18, FontWeight.SemiBold),
    // Card titles and section labels
    titleSmall = style(14, FontWeight.SemiBold),
    // Regular body and form text
    bodyMedium = style(14, FontWeight.Normal),
    // Card details and helper notes
    bodySmall = style(12, FontWeight.Normal),
    // Button labels
    labelLarge = style(14, FontWeight.Medium, 16.sp),
    // Filter chip labels
    labelMedium = style(12, FontWeight.Normal, 14.sp),
    // Bottom navigation labels
    labelSmall = style(11, FontWeight.Normal, 13.sp)
)
