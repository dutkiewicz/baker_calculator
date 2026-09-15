package com.bakersmath.domain

import kotlin.math.roundToInt

fun buildRecipeShareText(
    recipe: RecipeResult,
    hydrationPercent: Float,
): String {
    val loafEstimate =
        if (recipe.totalGrams < recipe.loafSizeGrams) {
            "< 1 loaf at ${recipe.loafSizeGrams}g"
        } else {
            "≈ ${recipe.estimatedLoaves} loaves at ${recipe.loafSizeGrams}g"
        }

    return buildString {
        appendLine("Baker's Math Recipe")
        appendLine("-------------------")
        appendLine("Flour:  ${recipe.flourGrams}g  (100%)")
        appendLine("Water:  ${recipe.waterGrams}g   (${hydrationPercent.roundToInt()}%)")
        appendLine("Salt:    ${recipe.saltGrams}g  ($SALT_PERCENT%)")
        appendLine("Yeast:    ${recipe.yeastGrams}g  ($YEAST_PERCENT%)")
        appendLine("-------------------")
        appendLine("Total:  ${recipe.totalGrams}g")
        append(loafEstimate)
    }
}
