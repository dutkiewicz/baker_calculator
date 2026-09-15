package com.bakersmath.domain

import kotlin.math.ceil
import kotlin.math.roundToInt

fun calculateIngredients(
    flourGrams: Int,
    hydrationPercent: Float,
): RecipeResult {
    val water = (flourGrams * hydrationPercent / 100f).roundToInt()
    val salt = (flourGrams * SALT_PERCENT / 100f).roundToInt()
    val yeast = (flourGrams * YEAST_PERCENT / 100f).roundToInt()
    val total = flourGrams + water + salt + yeast
    val loaves = ceil(total.toFloat() / DEFAULT_LOAF_SIZE_GRAMS).toInt()
    return RecipeResult(flourGrams, water, salt, yeast, total, loaves)
}
