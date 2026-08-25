package com.bakersmath.viewmodel

import com.bakersmath.domain.HydrationPreset
import com.bakersmath.domain.RecipeResult

data class UiState(
    val flourInput: String = "",
    val flourGrams: Int? = null,
    val hydrationPercent: Float = 65f,
    val activePreset: HydrationPreset? = HydrationPreset.STANDARD,
    val recipe: RecipeResult? = null,
    val isDarkMode: Boolean = false,
    val flourInputError: Boolean = false,
)
