package com.bakersmath.ui.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.bakersmath.domain.HydrationPreset
import com.bakersmath.domain.RecipeResult
import com.bakersmath.ui.BakersMathScreenContent
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.viewmodel.UiState

private val sampleRecipe =
    RecipeResult(
        flourGrams = 500,
        waterGrams = 325,
        saltGrams = 10,
        yeastGrams = 5,
        totalGrams = 840,
        estimatedLoaves = 2,
    )

private val filledState =
    UiState(
        flourInput = "500",
        flourGrams = 500,
        hydrationPercent = 65f,
        activePreset = HydrationPreset.STANDARD,
        recipe = sampleRecipe,
    )

private val emptyState =
    UiState(
        flourInput = "",
        flourGrams = null,
        hydrationPercent = 65f,
        activePreset = HydrationPreset.STANDARD,
        recipe = null,
    )

private val errorState =
    UiState(
        flourInput = "999999",
        flourGrams = 99_999,
        hydrationPercent = 60f,
        activePreset = HydrationPreset.STIFF,
        recipe = sampleRecipe.copy(flourGrams = 99_999),
        flourInputError = true,
    )

@Composable
private fun Screen(
    uiState: UiState,
    isDarkMode: Boolean,
) {
    BreadTheme(isDarkMode = isDarkMode) {
        BakersMathScreenContent(
            uiState = uiState.copy(isDarkMode = isDarkMode),
            onFlourChanged = {},
            onHydrationChanged = {},
            onPresetSelected = {},
            onThemeToggled = {},
            onShare = {},
        )
    }
}

// 1. Smallest common phone: 320dp width, short viewport.
@PreviewTest
@Preview(name = "Tiny 320x640 — Light", device = "spec:width=320dp,height=640dp,dpi=320")
@Composable
fun Tiny320x640Light() = Screen(filledState, isDarkMode = false)

@PreviewTest
@Preview(name = "Tiny 320x640 — Dark", device = "spec:width=320dp,height=640dp,dpi=320")
@Composable
fun Tiny320x640Dark() = Screen(filledState, isDarkMode = true)

// 2. Samsung Galaxy S24 / S25 class.
@PreviewTest
@Preview(name = "Galaxy S24 360x780 — Light", device = "spec:width=360dp,height=780dp,dpi=420")
@Composable
fun GalaxyS24Light() = Screen(filledState, isDarkMode = false)

@PreviewTest
@Preview(name = "Galaxy S24 360x780 — Dark", device = "spec:width=360dp,height=780dp,dpi=420")
@Composable
fun GalaxyS24Dark() = Screen(filledState, isDarkMode = true)

// 3. Samsung Galaxy A series class.
@PreviewTest
@Preview(name = "Galaxy A54 384x854 — Light", device = "spec:width=384dp,height=854dp,dpi=400")
@Composable
fun GalaxyA54Light() = Screen(filledState, isDarkMode = false)

@PreviewTest
@Preview(name = "Galaxy A54 384x854 — Dark", device = "spec:width=384dp,height=854dp,dpi=400")
@Composable
fun GalaxyA54Dark() = Screen(filledState, isDarkMode = true)

// 4. Pixel 8 class.
@PreviewTest
@Preview(name = "Pixel 8 412x915 — Light", device = "spec:width=412dp,height=915dp,dpi=420")
@Composable
fun Pixel8Light() = Screen(filledState, isDarkMode = false)

@PreviewTest
@Preview(name = "Pixel 8 412x915 — Dark", device = "spec:width=412dp,height=915dp,dpi=420")
@Composable
fun Pixel8Dark() = Screen(filledState, isDarkMode = true)

// 5. Pixel 8 Pro class (wider, denser).
@PreviewTest
@Preview(name = "Pixel 8 Pro 448x997 — Light", device = "spec:width=448dp,height=997dp,dpi=480")
@Composable
fun Pixel8ProLight() = Screen(filledState, isDarkMode = false)

@PreviewTest
@Preview(name = "Pixel 8 Pro 448x997 — Dark", device = "spec:width=448dp,height=997dp,dpi=480")
@Composable
fun Pixel8ProDark() = Screen(filledState, isDarkMode = true)

// 6. Foldable cover screen (narrow and tall).
@PreviewTest
@Preview(name = "Foldable cover 344x882 — Light", device = "spec:width=344dp,height=882dp,dpi=460")
@Composable
fun FoldableCoverLight() = Screen(filledState, isDarkMode = false)

@PreviewTest
@Preview(name = "Foldable cover 344x882 — Dark", device = "spec:width=344dp,height=882dp,dpi=460")
@Composable
fun FoldableCoverDark() = Screen(filledState, isDarkMode = true)

// 7. Foldable inner screen.
@PreviewTest
@Preview(name = "Foldable open 673x841 — Light", device = "spec:width=673dp,height=841dp,dpi=420")
@Composable
fun FoldableOpenLight() = Screen(filledState, isDarkMode = false)

@PreviewTest
@Preview(name = "Foldable open 673x841 — Dark", device = "spec:width=673dp,height=841dp,dpi=420")
@Composable
fun FoldableOpenDark() = Screen(filledState, isDarkMode = true)

// 8. Small tablet.
@PreviewTest
@Preview(name = "Tablet 800x1280 — Light", device = "spec:width=800dp,height=1280dp,dpi=240")
@Composable
fun TabletLight() = Screen(filledState, isDarkMode = false)

@PreviewTest
@Preview(name = "Tablet 800x1280 — Dark", device = "spec:width=800dp,height=1280dp,dpi=240")
@Composable
fun TabletDark() = Screen(filledState, isDarkMode = true)

// Accessibility: large system font scale on the smallest and a large phone.
@PreviewTest
@Preview(
    name = "Tiny 320x640 — Light, fontScale 1.3",
    device = "spec:width=320dp,height=640dp,dpi=320",
    fontScale = 1.3f,
)
@Composable
fun Tiny320x640LightLargeFont() = Screen(filledState, isDarkMode = false)

@PreviewTest
@Preview(
    name = "Pixel 8 412x915 — Light, fontScale 1.3",
    device = "spec:width=412dp,height=915dp,dpi=420",
    fontScale = 1.3f,
)
@Composable
fun Pixel8LightLargeFont() = Screen(filledState, isDarkMode = false)

// Empty and error states on the smallest phone.
@PreviewTest
@Preview(name = "Tiny 320x640 — Empty", device = "spec:width=320dp,height=640dp,dpi=320")
@Composable
fun Tiny320x640Empty() = Screen(emptyState, isDarkMode = false)

@PreviewTest
@Preview(name = "Tiny 320x640 — Error", device = "spec:width=320dp,height=640dp,dpi=320")
@Composable
fun Tiny320x640Error() = Screen(errorState, isDarkMode = false)
