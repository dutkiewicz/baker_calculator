package com.bakersmath.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bakersmath.domain.RecipeResult
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.ui.theme.LocalBreadColors

@Composable
fun TotalDoughCard(
    recipe: RecipeResult?,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.backgroundCard),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total Dough",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
            )

            Text(
                text = if (recipe != null) "${recipe.totalGrams}g" else "—",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.textPrimary,
            )

            val loafEstimate = when {
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
            recipe = RecipeResult(
                flourGrams = 500,
                waterGrams = 325,
                saltGrams = 10,
                yeastGrams = 5,
                totalGrams = 840,
                estimatedLoaves = 2,
            ),
        )
    }
}

@Preview(name = "TotalDoughCard — less than one loaf", showBackground = true)
@Composable
private fun TotalDoughCardSmallPreview() {
    BreadTheme(isDarkMode = false) {
        TotalDoughCard(
            recipe = RecipeResult(
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

@Preview(name = "TotalDoughCard — Dark", showBackground = true, backgroundColor = 0xFF1E1108)
@Composable
private fun TotalDoughCardDarkPreview() {
    BreadTheme(isDarkMode = true) {
        TotalDoughCard(
            recipe = RecipeResult(
                flourGrams = 500,
                waterGrams = 325,
                saltGrams = 10,
                yeastGrams = 5,
                totalGrams = 840,
                estimatedLoaves = 2,
            ),
        )
    }
}
