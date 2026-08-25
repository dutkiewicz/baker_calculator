package com.bakersmath.ui.theme

import androidx.compose.ui.graphics.Color

data class BreadColors(
    val backgroundPrimary: Color,
    val backgroundCard: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val flourDot: Color,
    val waterDot: Color,
    val saltDot: Color,
    val yeastDot: Color,
    val chipSelectedBackground: Color,
    val chipSelectedText: Color,
    val chipOutlineColor: Color,
)

val LightBreadColors = BreadColors(
    backgroundPrimary = Color(0xFFF5ECD7),
    backgroundCard = Color(0xFFEDE0C4),
    textPrimary = Color(0xFF3B1F0A),
    textSecondary = Color(0xFF7A5230),
    accent = Color(0xFFC07D3A),
    flourDot = Color(0xFFF0D9A8),
    waterDot = Color(0xFF7BB3D4),
    saltDot = Color(0xFFD4CFC9),
    yeastDot = Color(0xFFC8A96A),
    chipSelectedBackground = Color(0xFFC07D3A),
    chipSelectedText = Color(0xFFF5ECD7),
    chipOutlineColor = Color(0xFF7A5230),
)

val DarkBreadColors = BreadColors(
    backgroundPrimary = Color(0xFF1E1108),
    backgroundCard = Color(0xFF2D1A0A),
    textPrimary = Color(0xFFF5ECD7),
    textSecondary = Color(0xFFD4B896),
    accent = Color(0xFFD4913F),
    flourDot = Color(0xFFF0D9A8),
    waterDot = Color(0xFF7BB3D4),
    saltDot = Color(0xFFD4CFC9),
    yeastDot = Color(0xFFC8A96A),
    chipSelectedBackground = Color(0xFFD4913F),
    chipSelectedText = Color(0xFF1E1108),
    chipOutlineColor = Color(0xFFD4B896),
)
