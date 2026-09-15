package com.bakersmath.viewmodel

import com.bakersmath.domain.HydrationPreset

sealed interface BakersMathEvent {
    data class FlourChanged(val raw: String) : BakersMathEvent

    data class HydrationChanged(val percent: Float) : BakersMathEvent

    data class PresetSelected(val preset: HydrationPreset) : BakersMathEvent

    data object ThemeToggled : BakersMathEvent
}
