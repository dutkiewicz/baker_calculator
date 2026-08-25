package com.bakersmath.domain

data class RecipeResult(
    val flourGrams: Int,
    val waterGrams: Int,
    val saltGrams: Int,
    val yeastGrams: Int,
    val totalGrams: Int,
    val estimatedLoaves: Int,
    val loafSizeGrams: Int = DEFAULT_LOAF_SIZE_GRAMS,
)
