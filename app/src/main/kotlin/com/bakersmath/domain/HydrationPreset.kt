package com.bakersmath.domain

enum class HydrationPreset(val percent: Float, val style: String) {
    STIFF(60f, "Stiff"),
    STANDARD(65f, "Standard"),
    HEARTH(75f, "Hearth"),
    CIABATTA(85f, "Ciabatta"),
}
