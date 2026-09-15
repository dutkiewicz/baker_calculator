package com.bakersmath.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HydrationPreset.entries.forEach { preset ->
            HydrationChip(
                preset = preset,
                selected = activePreset == preset,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onPresetSelected(preset)
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/**
 * A compact, single-line chip. Unlike Material's [androidx.compose.material3.FilterChip]
 * the horizontal padding is small enough that the labels fit on narrow phones such as
 * the Samsung Galaxy S25, while the clickable area still meets the 48dp touch target.
 */
@Composable
private fun HydrationChip(
    preset: HydrationPreset,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier =
            modifier
                .height(48.dp)
                .selectable(
                    selected = selected,
                    role = Role.RadioButton,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(shape)
                    .background(
                        if (selected) colors.chipSelectedBackground else Color.Transparent,
                    )
                    .border(
                        width = 1.dp,
                        color = if (selected) Color.Transparent else colors.chipOutlineColor,
                        shape = shape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "${preset.label} · ${preset.style}",
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) colors.chipSelectedText else colors.textPrimary,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 6.dp),
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
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
