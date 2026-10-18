package ru.androidlabs.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.androidlabs.R

val YsDisplay = FontFamily(
    Font(R.font.ys_display_regular, FontWeight.Normal),
    Font(R.font.ys_display_medium, FontWeight.Medium)
)

val Typography = Typography(
    bodyLarge = TextStyle(fontFamily = YsDisplay, fontSize = 16.sp, letterSpacing = 0.sp),
    bodyMedium = TextStyle(fontFamily = YsDisplay, fontSize = 14.sp, letterSpacing = 0.sp),
    titleLarge = TextStyle(fontFamily = YsDisplay, fontWeight = FontWeight.Medium, fontSize = 22.sp, letterSpacing = 0.sp),
    labelLarge = TextStyle(fontFamily = YsDisplay, fontWeight = FontWeight.Medium, fontSize = 16.sp, letterSpacing = 0.sp)
)
