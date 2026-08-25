package com.bakersmath.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bakersmath.domain.RecipeResult
import com.bakersmath.domain.YeastType
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.ui.theme.LocalBreadColors
import kotlin.math.roundToInt

@Composable
fun IngredientsList(
    recipe: RecipeResult?,
    hydrationPercent: Float,
    yeastType: YeastType,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current

    Column(modifier = modifier) {
        IngredientRow(
            name = "Flour",
            colorDot = colors.flourDot,
            percentLabel = "100%",
            gramsLabel = recipe?.let { "${it.flourGrams}g" } ?: "—",
        )

        HorizontalDivider()

        IngredientRow(
            name = "Water",
            colorDot = colors.waterDot,
            percentLabel = "${hydrationPercent.roundToInt()}%",
            gramsLabel = recipe?.let { "${it.waterGrams}g" } ?: "—",
        )

        HorizontalDivider()

        IngredientRow(
            name = "Salt",
            colorDot = colors.saltDot,
            percentLabel = "2%",
            gramsLabel = recipe?.let { "${it.saltGrams}g" } ?: "—",
        )

        HorizontalDivider()

        IngredientRow(
            name = "Yeast",
            colorDot = colors.yeastDot,
            percentLabel = "1%",
            gramsLabel = recipe?.let { "${it.yeastGrams}g" } ?: "—",
            subtitle = yeastType.displayName,
        )
    }
}

@Preview(name = "IngredientsList — null recipe", showBackground = true)
@Composable
private fun IngredientsListNullPreview() {
    BreadTheme(isDarkMode = false) {
        IngredientsList(
            recipe = null,
            hydrationPercent = 65f,
            yeastType = YeastType.INSTANT_DRY,
        )
    }
}

@Preview(name = "IngredientsList — with recipe", showBackground = true)
@Composable
private fun IngredientsListWithRecipePreview() {
    BreadTheme(isDarkMode = false) {
        IngredientsList(
            recipe = RecipeResult(
                flourGrams = 500,
                waterGrams = 325,
                saltGrams = 10,
                yeastGrams = 5,
                totalGrams = 840,
                estimatedLoaves = 2,
            ),
            hydrationPercent = 65f,
            yeastType = YeastType.INSTANT_DRY,
        )
    }
}

@Preview(name = "IngredientsList — Dark, fresh yeast", showBackground = true, backgroundColor = 0xFF1E1108)
@Composable
private fun IngredientsListDarkPreview() {
    BreadTheme(isDarkMode = true) {
        IngredientsList(
            recipe = RecipeResult(
                flourGrams = 500,
                waterGrams = 325,
                saltGrams = 10,
                yeastGrams = 5,
                totalGrams = 840,
                estimatedLoaves = 2,
            ),
            hydrationPercent = 65f,
            yeastType = YeastType.FRESH,
        )
    }
}
