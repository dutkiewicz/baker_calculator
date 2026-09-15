package com.bakersmath.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class RecipeCalculatorTest {
    @Test
    fun `flour 500 hydration 65 produces correct recipe`() {
        val result = calculateIngredients(500, 65f)
        assertEquals(325, result.waterGrams)
        assertEquals(10, result.saltGrams)
        assertEquals(5, result.yeastGrams)
        assertEquals(840, result.totalGrams)
        assertEquals(2, result.estimatedLoaves)
    }

    @Test
    fun `flour 1000 hydration 65 produces correct recipe`() {
        val result = calculateIngredients(1000, 65f)
        assertEquals(650, result.waterGrams)
        assertEquals(20, result.saltGrams)
        assertEquals(10, result.yeastGrams)
        assertEquals(1680, result.totalGrams)
        assertEquals(4, result.estimatedLoaves)
    }

    @Test
    fun `flour 500 hydration 75 produces correct recipe`() {
        val result = calculateIngredients(500, 75f)
        assertEquals(375, result.waterGrams)
        assertEquals(890, result.totalGrams)
        assertEquals(2, result.estimatedLoaves)
    }

    @Test
    fun `flour 400 hydration 65 gives 2 loaves via ceiling`() {
        val result = calculateIngredients(400, 65f)
        assertEquals(672, result.totalGrams)
        assertEquals(2, result.estimatedLoaves)
    }

    @Test
    fun `flour 200 hydration 65 gives 1 loaf`() {
        val result = calculateIngredients(200, 65f)
        assertEquals(336, result.totalGrams)
        assertEquals(1, result.estimatedLoaves)
    }

    @Test
    fun `rounds weights to nearest gram`() {
        val result = calculateIngredients(250, 65f)
        assertEquals(163, result.waterGrams)
        assertEquals(5, result.saltGrams)
        assertEquals(3, result.yeastGrams)
        assertEquals(421, result.totalGrams)
    }

    @Test
    fun `minimum flour and hydration clamp computes without negative values`() {
        val result = calculateIngredients(1, 45f)
        assertEquals(0, result.waterGrams)
        assertEquals(1, result.totalGrams)
        assertEquals(1, result.estimatedLoaves)
    }

    @Test
    fun `maximum hydration 100 percent doubles the water weight`() {
        val result = calculateIngredients(500, 100f)
        assertEquals(500, result.waterGrams)
        assertEquals(1015, result.totalGrams)
        assertEquals(3, result.estimatedLoaves)
    }

    @Test
    fun `loaf size defaults to the configured constant`() {
        val result = calculateIngredients(500, 65f)
        assertEquals(DEFAULT_LOAF_SIZE_GRAMS, result.loafSizeGrams)
    }
}
