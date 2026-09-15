package com.bakersmath.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.ui.theme.LocalBreadColors
import com.bakersmath.ui.theme.PREVIEW_DARK_BACKGROUND

@Composable
fun IngredientRow(
    name: String,
    colorDot: Color,
    percentLabel: String,
    gramsLabel: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {},
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left: dot + name/subtitle
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(colorDot),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.textPrimary,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary,
                    )
                }
            }
        }

        // Right: grams + percent
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = gramsLabel,
                style = MaterialTheme.typography.titleLarge,
                color = colors.textPrimary,
            )
            Text(
                text = percentLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
            )
        }
    }
}

@Preview(name = "IngredientRow — with subtitle", showBackground = true)
@Composable
private fun IngredientRowWithSubtitlePreview() {
    BreadTheme(isDarkMode = false) {
        val colors = LocalBreadColors.current
        IngredientRow(
            name = "Yeast",
            colorDot = colors.yeastDot,
            percentLabel = "1%",
            gramsLabel = "5g",
            subtitle = "instant dry",
        )
    }
}

@Preview(name = "IngredientRow — no subtitle, Dark", showBackground = true, backgroundColor = PREVIEW_DARK_BACKGROUND)
@Composable
private fun IngredientRowNoSubtitleDarkPreview() {
    BreadTheme(isDarkMode = true) {
        val colors = LocalBreadColors.current
        IngredientRow(
            name = "Flour",
            colorDot = colors.flourDot,
            percentLabel = "100%",
            gramsLabel = "500g",
        )
    }
}
