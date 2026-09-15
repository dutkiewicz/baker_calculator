package com.bakersmath.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bakersmath.domain.HydrationPreset
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.ui.theme.LocalBreadColors
import com.bakersmath.ui.theme.PREVIEW_DARK_BACKGROUND

@Composable
fun HydrationPresets(
    activePreset: HydrationPreset?,
    onPresetSelected: (HydrationPreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HydrationPreset.entries.forEach { preset ->
            val selected = activePreset == preset
            FilterChip(
                selected = selected,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onPresetSelected(preset)
                },
                label = {
                    Text("${preset.label} · ${preset.style}")
                },
                colors =
                    FilterChipDefaults.filterChipColors(
                        containerColor = Color.Transparent,
                        labelColor = colors.textPrimary,
                        selectedContainerColor = colors.chipSelectedBackground,
                        selectedLabelColor = colors.chipSelectedText,
                    ),
                border =
                    FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selected,
                        borderColor = colors.chipOutlineColor,
                        selectedBorderColor = Color.Transparent,
                    ),
            )
        }
    }
}

@Preview(name = "HydrationPresets — STANDARD selected", showBackground = true)
@Composable
private fun HydrationPresetsPreview() {
    BreadTheme(isDarkMode = false) {
        HydrationPresets(
            activePreset = HydrationPreset.STANDARD,
            onPresetSelected = {},
        )
    }
}

@Preview(
    name = "HydrationPresets — Dark, HEARTH selected",
    showBackground = true,
    backgroundColor = PREVIEW_DARK_BACKGROUND,
)
@Composable
private fun HydrationPresetsDarkPreview() {
    BreadTheme(isDarkMode = true) {
        HydrationPresets(
            activePreset = HydrationPreset.HEARTH,
            onPresetSelected = {},
        )
    }
}
