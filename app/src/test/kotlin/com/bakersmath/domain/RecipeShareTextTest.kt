package com.bakersmath.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeShareTextTest {
    private val recipe =
        RecipeResult(
            flourGrams = 500,
            waterGrams = 325,
            saltGrams = 10,
            yeastGrams = 5,
            totalGrams = 840,
            estimatedLoaves = 2,
        )

    @Test
    fun `share text contains every ingredient with grams and percent`() {
        val text = buildRecipeShareText(recipe, 65f)

        assertTrue(text.contains("Baker's Math Recipe"))
        assertTrue(text.contains("Flour:  500g  (100%)"))
        assertTrue(text.contains("Water:  325g   (65%)"))
        assertTrue(text.contains("Salt:    10g  (2.0%)"))
        assertTrue(text.contains("Yeast:    5g  (1.0%)"))
        assertTrue(text.contains("Total:  840g"))
        assertTrue(text.contains("≈ 2 loaves at 450g"))
    }

    @Test
    fun `share text reflects the current hydration`() {
        val text = buildRecipeShareText(recipe, 75f)
        assertTrue(text.contains("(75%)"))
    }

    @Test
    fun `share text reports less than one loaf for small batches`() {
        val small =
            RecipeResult(
                flourGrams = 200,
                waterGrams = 130,
                saltGrams = 4,
                yeastGrams = 2,
                totalGrams = 336,
                estimatedLoaves = 0,
            )
        val text = buildRecipeShareText(small, 65f)
        assertTrue(text.contains("< 1 loaf at 450g"))
    }

    @Test
    fun `share text is plain text with no markdown`() {
        val text = buildRecipeShareText(recipe, 65f)
        assertTrue(!text.contains("**"))
        assertTrue(!text.contains("#"))
    }
}
