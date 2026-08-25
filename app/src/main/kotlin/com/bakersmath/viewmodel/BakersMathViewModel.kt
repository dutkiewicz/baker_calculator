package com.bakersmath.viewmodel

import androidx.lifecycle.ViewModel
import com.bakersmath.domain.FLOUR_MAX_GRAMS
import com.bakersmath.domain.FLOUR_MIN_GRAMS
import com.bakersmath.domain.HydrationPreset
import com.bakersmath.domain.calculateIngredients
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BakersMathViewModel(isDarkMode: Boolean = false) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(isDarkMode = isDarkMode))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onEvent(event: BakersMathEvent) {
        when (event) {
            is BakersMathEvent.FlourChanged -> handleFlourChanged(event.raw)
            is BakersMathEvent.HydrationChanged -> handleHydrationChanged(event.percent)
            is BakersMathEvent.PresetSelected -> handlePresetSelected(event.preset)
            is BakersMathEvent.ThemeToggled -> _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
        }
    }

    private fun handleFlourChanged(raw: String) {
        val digits = raw.filter { it.isDigit() }
        val parsed = digits.toIntOrNull()

        if (parsed == null) {
            _uiState.update {
                it.copy(flourInput = digits, flourGrams = null, recipe = null, flourInputError = false)
            }
            return
        }

        if (parsed > FLOUR_MAX_GRAMS) {
            _uiState.update {
                it.copy(
                    flourInput = digits,
                    flourGrams = FLOUR_MAX_GRAMS,
                    flourInputError = true,
                    recipe = calculateIngredients(FLOUR_MAX_GRAMS, it.hydrationPercent),
                )
            }
            return
        }

        val clamped = parsed.coerceAtLeast(FLOUR_MIN_GRAMS)
        _uiState.update {
            it.copy(
                flourInput = digits,
                flourGrams = clamped,
                flourInputError = false,
                recipe = calculateIngredients(clamped, it.hydrationPercent),
            )
        }
    }

    private fun handleHydrationChanged(percent: Float) {
        val matched = HydrationPreset.entries.find { it.percent == percent }
        _uiState.update { state ->
            val newState = state.copy(hydrationPercent = percent, activePreset = matched)
            if (state.flourGrams != null) {
                newState.copy(recipe = calculateIngredients(state.flourGrams, percent))
            } else {
                newState
            }
        }
    }

    private fun handlePresetSelected(preset: HydrationPreset) {
        _uiState.update { state ->
            val newState = state.copy(hydrationPercent = preset.percent, activePreset = preset)
            if (state.flourGrams != null) {
                newState.copy(recipe = calculateIngredients(state.flourGrams, preset.percent))
            } else {
                newState
            }
        }
    }
}
