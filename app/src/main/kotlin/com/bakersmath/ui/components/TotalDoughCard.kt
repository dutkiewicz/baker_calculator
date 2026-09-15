package com.bakersmath.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bakersmath.domain.RecipeResult
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.ui.theme.LocalBreadColors
import com.bakersmath.ui.theme.PREVIEW_DARK_BACKGROUND

@Composable
fun TotalDoughCard(
    recipe: RecipeResult?,
    onShare: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.backgroundCard),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Total Dough",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary,
                )
                if (recipe != null && onShare != null) {
                    IconButton(onClick = onShare) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share recipe",
                            tint = colors.accent,
                        )
                    }
                }
            }

            Text(
                text = if (recipe != null) "${recipe.totalGrams}g" else "—",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.textPrimary,
            )

            val loafEstimate =
                when {
                    recipe == null -> "—"
                    recipe.totalGrams < recipe.loafSizeGrams -> "< 1 loaf at ${recipe.loafSizeGrams}g"
                    else -> "≈ ${recipe.estimatedLoaves} loaves at ${recipe.loafSizeGrams}g"
                }
            Text(
                text = loafEstimate,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
            )
        }
    }
}

@Preview(name = "TotalDoughCard — null recipe", showBackground = true)
@Composable
private fun TotalDoughCardNullPreview() {
    BreadTheme(isDarkMode = false) {
        TotalDoughCard(recipe = null)
    }
}

@Preview(name = "TotalDoughCard — 840g, 2 loaves", showBackground = true)
@Composable
private fun TotalDoughCardWithRecipePreview() {
    BreadTheme(isDarkMode = false) {
        TotalDoughCard(
            recipe =
                RecipeResult(
                    flourGrams = 500,
                    waterGrams = 325,
                    saltGrams = 10,
                    yeastGrams = 5,
                    totalGrams = 840,
                    estimatedLoaves = 2,
                ),
            onShare = {},
        )
    }
}

@Preview(name = "TotalDoughCard — less than one loaf", showBackground = true)
@Composable
private fun TotalDoughCardSmallPreview() {
    BreadTheme(isDarkMode = false) {
        TotalDoughCard(
            recipe =
                RecipeResult(
                    flourGrams = 200,
                    waterGrams = 130,
                    saltGrams = 4,
                    yeastGrams = 2,
                    totalGrams = 336,
                    estimatedLoaves = 0,
                    loafSizeGrams = 450,
                ),
        )
    }
}

@Preview(
    name = "TotalDoughCard — Dark",
    showBackground = true,
    backgroundColor = PREVIEW_DARK_BACKGROUND,
)
@Composable
private fun TotalDoughCardDarkPreview() {
    BreadTheme(isDarkMode = true) {
        TotalDoughCard(
            recipe =
                RecipeResult(
                    flourGrams = 500,
                    waterGrams = 325,
                    saltGrams = 10,
                    yeastGrams = 5,
                    totalGrams = 840,
                    estimatedLoaves = 2,
                ),
            onShare = {},
        )
    }
}
