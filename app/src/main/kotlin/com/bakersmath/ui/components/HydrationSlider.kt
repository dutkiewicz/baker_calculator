package com.bakersmath.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.bakersmath.domain.HYDRATION_MAX
import com.bakersmath.domain.HYDRATION_MIN
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.ui.theme.LocalBreadColors
import com.bakersmath.ui.theme.PREVIEW_DARK_BACKGROUND
import kotlin.math.roundToInt

@Composable
fun HydrationSlider(
    hydrationPercent: Float,
    onHydrationChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current

    Column(modifier = modifier) {
        Text(
            text = "${hydrationPercent.roundToInt()}%",
            style = MaterialTheme.typography.titleLarge,
            color = colors.textPrimary,
        )

        Slider(
            value = hydrationPercent,
            onValueChange = onHydrationChanged,
            valueRange = HYDRATION_MIN..HYDRATION_MAX,
            colors =
                SliderDefaults.colors(
                    thumbColor = colors.accent,
                    activeTrackColor = colors.accent,
                ),
            modifier =
                Modifier.semantics {
                    contentDescription = "Hydration slider, ${hydrationPercent.roundToInt()} percent"
                },
        )
    }
}

@Preview(name = "HydrationSlider — Light", showBackground = true)
@Composable
private fun HydrationSliderLightPreview() {
    BreadTheme(isDarkMode = false) {
        HydrationSlider(
            hydrationPercent = 65f,
            onHydrationChanged = {},
        )
    }
}

@Preview(name = "HydrationSlider — Dark", showBackground = true, backgroundColor = PREVIEW_DARK_BACKGROUND)
@Composable
private fun HydrationSliderDarkPreview() {
    BreadTheme(isDarkMode = true) {
        HydrationSlider(
            hydrationPercent = 75f,
            onHydrationChanged = {},
        )
    }
}
