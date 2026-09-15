package com.bakersmath.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.ui.theme.LocalBreadColors
import com.bakersmath.ui.theme.PREVIEW_DARK_BACKGROUND

@Composable
fun ThemeToggle(
    isDarkMode: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current
    IconButton(onClick = onToggle, modifier = modifier) {
        if (isDarkMode) {
            Icon(
                imageVector = Icons.Rounded.NightsStay,
                contentDescription = "Switch to light mode",
                tint = colors.accent,
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.WbSunny,
                contentDescription = "Switch to dark mode",
                tint = colors.accent,
            )
        }
    }
}

@Preview(name = "ThemeToggle — Light mode (sun shown)", showBackground = true)
@Composable
private fun ThemeToggleLightPreview() {
    BreadTheme(isDarkMode = false) {
        ThemeToggle(isDarkMode = false, onToggle = {})
    }
}

@Preview(name = "ThemeToggle — Dark mode (moon shown)", showBackground = true, backgroundColor = PREVIEW_DARK_BACKGROUND)
@Composable
private fun ThemeToggleDarkPreview() {
    BreadTheme(isDarkMode = true) {
        ThemeToggle(isDarkMode = true, onToggle = {})
    }
}
