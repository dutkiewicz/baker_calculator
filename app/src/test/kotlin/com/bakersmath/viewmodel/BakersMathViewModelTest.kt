package com.bakersmath.viewmodel

import app.cash.turbine.test
import com.bakersmath.domain.HydrationPreset
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BakersMathViewModelTest {

    private fun viewModel(isDarkMode: Boolean = false) = BakersMathViewModel(isDarkMode)

    @Test
    fun `FlourChanged 500 produces totalGrams 840`() = runTest {
        val vm = viewModel()
        vm.onEvent(BakersMathEvent.FlourChanged("500"))
        assertEquals(840, vm.uiState.value.recipe!!.totalGrams)
    }

    @Test
    fun `FlourChanged empty clears recipe without crash`() = runTest {
        val vm = viewModel()
        vm.onEvent(BakersMathEvent.FlourChanged("500"))
        vm.onEvent(BakersMathEvent.FlourChanged(""))
        assertNull(vm.uiState.value.recipe)
    }

    @Test
    fun `FlourChanged over max clamps to 99999 and sets error`() = runTest {
        val vm = viewModel()
        vm.onEvent(BakersMathEvent.FlourChanged("100000"))
        assertEquals(99_999, vm.uiState.value.flourGrams)
        assertTrue(vm.uiState.value.flourInputError)
    }

    @Test
    fun `HydrationChanged 65 matches STANDARD preset`() = runTest {
        val vm = viewModel()
        vm.onEvent(BakersMathEvent.HydrationChanged(65f))
        assertEquals(HydrationPreset.STANDARD, vm.uiState.value.activePreset)
    }

    @Test
    fun `HydrationChanged 70 matches no preset`() = runTest {
        val vm = viewModel()
        vm.onEvent(BakersMathEvent.HydrationChanged(70f))
        assertNull(vm.uiState.value.activePreset)
    }

    @Test
    fun `PresetSelected HEARTH sets hydration and preset`() = runTest {
        val vm = viewModel()
        vm.onEvent(BakersMathEvent.PresetSelected(HydrationPreset.HEARTH))
        assertEquals(75f, vm.uiState.value.hydrationPercent)
        assertEquals(HydrationPreset.HEARTH, vm.uiState.value.activePreset)
    }

    @Test
    fun `ThemeToggled twice returns to original value`() = runTest {
        val vm = viewModel(isDarkMode = false)
        vm.onEvent(BakersMathEvent.ThemeToggled)
        assertTrue(vm.uiState.value.isDarkMode)
        vm.onEvent(BakersMathEvent.ThemeToggled)
        assertFalse(vm.uiState.value.isDarkMode)
    }

    @Test
    fun `FlourChanged then HydrationChanged recalculates recipe`() = runTest {
        val vm = viewModel()
        vm.onEvent(BakersMathEvent.FlourChanged("500"))
        vm.onEvent(BakersMathEvent.HydrationChanged(75f))
        assertEquals(895, vm.uiState.value.recipe!!.totalGrams)
    }

    @Test
    fun `uiState emits correct sequence via Turbine`() = runTest {
        val vm = viewModel()
        vm.uiState.test {
            val initial = awaitItem()
            assertNull(initial.recipe)

            vm.onEvent(BakersMathEvent.FlourChanged("500"))
            val afterFlour = awaitItem()
            assertEquals(840, afterFlour.recipe!!.totalGrams)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
