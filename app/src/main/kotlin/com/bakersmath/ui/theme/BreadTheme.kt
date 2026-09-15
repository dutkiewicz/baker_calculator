package com.bakersmath.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BreadTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit,
) {
    val colors = if (isDarkMode) DarkBreadColors else LightBreadColors
    CompositionLocalProvider(LocalBreadColors provides colors) {
        MaterialTheme(typography = BreadTypography, content = content)
    }
}

@Preview(name = "BreadTheme — Light", showBackground = true)
@Composable
private fun BreadThemeLightPreview() {
    BreadTheme(isDarkMode = false) {
        Text(
            text = "Baker's Math",
            color = LocalBreadColors.current.textPrimary,
        )
    }
}

@Preview(name = "BreadTheme — Dark", showBackground = true, backgroundColor = PREVIEW_DARK_BACKGROUND)
@Composable
private fun BreadThemeDarkPreview() {
    BreadTheme(isDarkMode = true) {
        Text(
            text = "Baker's Math",
            color = LocalBreadColors.current.textPrimary,
        )
    }
}
