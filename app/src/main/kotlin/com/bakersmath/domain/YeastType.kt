package com.bakersmath.domain

enum class YeastType(val displayName: String, val multiplier: Float) {
    INSTANT_DRY("instant dry", 1.0f),
    ACTIVE_DRY("active dry", 1.1f),
    FRESH("fresh", 3.0f),
}
