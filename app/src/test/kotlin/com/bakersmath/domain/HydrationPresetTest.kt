package com.bakersmath.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HydrationPresetTest {
    @Test
    fun `presets expose the three documented hydration levels`() {
        assertEquals(3, HydrationPreset.entries.size)
        assertEquals(60f, HydrationPreset.STIFF.percent)
        assertEquals(65f, HydrationPreset.STANDARD.percent)
        assertEquals(75f, HydrationPreset.HEARTH.percent)
    }

    @Test
    fun `presets expose display labels and styles`() {
        assertEquals("60%", HydrationPreset.STIFF.label)
        assertEquals("Stiff", HydrationPreset.STIFF.style)
        assertEquals("65%", HydrationPreset.STANDARD.label)
        assertEquals("Standard", HydrationPreset.STANDARD.style)
        assertEquals("75%", HydrationPreset.HEARTH.label)
        assertEquals("Hearth", HydrationPreset.HEARTH.style)
    }

    @Test
    fun `presets lie within the slider range`() {
        HydrationPreset.entries.forEach { preset ->
            assertTrue(preset.percent >= HYDRATION_MIN)
            assertTrue(preset.percent <= HYDRATION_MAX)
        }
    }
}
