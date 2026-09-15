package com.bakersmath.domain

enum class HydrationPreset(val percent: Float, val label: String, val style: String) {
    STIFF(60f, "60%", "Stiff"),
    STANDARD(65f, "65%", "Standard"),
    HEARTH(75f, "75%", "Hearth"),
    CIABATTA(85f, "85%", "Ciabatta"),
}
