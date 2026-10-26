package com.udb.convite.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BerryPrimary = Color(0xFFA32D5E)
val TealSecondary = Color(0xFF1B5E56)
val MarigoldAccent = Color(0xFFE8A33D)
val CoralAccent = Color(0xFFF2765F)
val WarmBackground = Color(0xFFFDFBF7)
val CardSurface = Color(0xFFFFFFFF)
val TextDark = Color(0xFF2C2C2C)
val TextMuted = Color(0xFF757575)

private val LightColorScheme = lightColorScheme(
    primary = BerryPrimary,
    secondary = TealSecondary,
    tertiary = MarigoldAccent,
    background = WarmBackground,
    surface = CardSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun ConviteTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
