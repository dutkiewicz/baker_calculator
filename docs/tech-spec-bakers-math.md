# Baker's Math — Technical Architecture Spec

## Overview

Full technical architecture for the Baker's Math Android app — a single-screen baker's percentage calculator. Implements the product spec dated 2026-05-11. Greenfield Android project; no existing codebase.

---

## Resolved Open Questions

**1. Hydration slider range**
Decision: slider range is 45%–100%. The four presets (60%, 65%, 75%, 85%) sit inside this range. Extending to 45% supports cracker doughs; extending to 100% supports wet ciabatta extremes. The spec's "50%–90%" is a practical approximation, not a hard business rule. Clamping at 90% would frustrate advanced users.

**2. Yeast type in v1**
Decision: static label "instant dry" in v1. Yeast weight stays `flour × 0.01`. The label is a `Text` composable backed by `YeastType.INSTANT_DRY.displayName` — not a hardcoded string — so v2 can drop in a selector over the same data model without layout changes.

**3. Flour input limits**
Decision: minimum 1g, maximum 99,999g. Zero/negative values are rejected; values above the max are clamped (not rejected) with an inline error indicator.

**4. Loaves estimate logic**
Decision: 450g default, not adjustable in v1. Formula: `ceil(totalDough / loafSizeGrams)`. If total < 450g: "< 1 loaf". `DEFAULT_LOAF_SIZE_GRAMS = 450` is a named constant so the nice-to-have loaf size selector can be wired up without touching calculation logic.

**5. Offline-only confirmed**
Decision: confirmed. No network permission in the manifest. No analytics, no crash reporting, no accounts. Sole persistence: Jetpack DataStore for the dark/light theme preference.

---

## Architecture Decisions

**Single-Activity, no Navigation component** — the spec mandates one screen. `MainActivity` hosts a single `BakersMathScreen` composable. No fragments, no NavHost.

**ViewModel + StateFlow** — a single `UiState` data class exposed as `StateFlow<UiState>`. All calculation logic lives in the ViewModel. No Redux-style library needed; the data flow is a simple pure derivation.

**Jetpack DataStore Preferences** for theme toggle persistence. One boolean key. Read once at startup with `runBlocking` (scoped to that one read, not a general pattern).

**MaterialTheme + custom `BreadColors` tokens via CompositionLocal** — opted out of Material You dynamic color deliberately. The bread-tone palette is brand-specific and must not follow system wallpaper color. `LocalBreadColors` provides tokens to all composables without prop-drilling.

**Pure Kotlin calculation, no math library** — baker's percentages are arithmetic. The calculation is a single top-level pure function, easily unit-tested.

---

## Tech Stack

| Layer | Technology | Notes |
|---|---|---|
| Language | Kotlin 2.x | |
| UI | Jetpack Compose (BOM 2024.12+) | No XML views |
| Architecture | MVVM — ViewModel + StateFlow | Standard AndroidX |
| Persistence | DataStore Preferences | Theme only |
| Build | Gradle Kotlin DSL + AGP 8.x | libs.versions.toml |
| Min SDK | API 26 (Android 8.0) | ~95% device coverage |
| Target SDK | API 35 | Edge-to-edge |
| Testing | JUnit 4 + Turbine | ViewModel unit tests; no UI tests in v1 |
| Linting | Ktlint via Gradle plugin | |

No Hilt/Dagger. Dependency graph is trivial (one ViewModel, one DataStore instance). Manual injection via Application class.

---

## Project Structure

```
app/src/main/kotlin/com/bakersmath/
  MainActivity.kt
  ui/
    BakersMathScreen.kt
    components/
      FlourInputSection.kt
      HydrationSlider.kt
      HydrationPresets.kt
      IngredientRow.kt
      IngredientsList.kt
      TotalDoughCard.kt
      ThemeToggle.kt
    theme/
      BreadColors.kt
      BreadTypography.kt
      BreadTheme.kt
      LocalBreadColors.kt
  viewmodel/
    BakersMathViewModel.kt
    UiState.kt
  domain/
    RecipeCalculator.kt
    RecipeResult.kt
    YeastType.kt
    Constants.kt
  data/
    ThemeRepository.kt

app/src/test/kotlin/com/bakersmath/
  domain/RecipeCalculatorTest.kt
  viewmodel/BakersMathViewModelTest.kt
```

---

## Data and State Model

```kotlin
data class UiState(
    val flourInput: String = "",
    val flourGrams: Int? = null,
    val hydrationPercent: Float = 65f,
    val activePreset: HydrationPreset? = HydrationPreset.STANDARD,
    val recipe: RecipeResult? = null,
    val isDarkMode: Boolean = false,
    val flourInputError: Boolean = false
)

data class RecipeResult(
    val flourGrams: Int,
    val waterGrams: Int,
    val saltGrams: Int,
    val yeastGrams: Int,
    val totalGrams: Int,
    val estimatedLoaves: Int,
    val loafSizeGrams: Int = DEFAULT_LOAF_SIZE_GRAMS
)

enum class HydrationPreset(val percent: Float, val label: String, val style: String) {
    STIFF(60f, "60%", "Stiff"),
    STANDARD(65f, "65%", "Standard"),
    HEARTH(75f, "75%", "Hearth"),
    CIABATTA(85f, "85%", "Ciabatta")
}

enum class YeastType(val displayName: String, val multiplier: Float) {
    INSTANT_DRY("instant dry", 1.0f),
    ACTIVE_DRY("active dry", 1.1f),   // stub for v2
    FRESH("fresh", 3.0f)               // stub for v2
}

sealed interface BakersMathEvent {
    data class FlourChanged(val raw: String) : BakersMathEvent
    data class HydrationChanged(val percent: Float) : BakersMathEvent
    data class PresetSelected(val preset: HydrationPreset) : BakersMathEvent
    object ThemeToggled : BakersMathEvent
}
```

---

## Calculation Logic (`RecipeCalculator.kt`)

```kotlin
fun calculateIngredients(flourGrams: Int, hydrationPercent: Float): RecipeResult {
    val water  = (flourGrams * hydrationPercent / 100f).roundToInt()
    val salt   = (flourGrams * SALT_PERCENT / 100f).roundToInt()
    val yeast  = (flourGrams * YEAST_PERCENT / 100f).roundToInt()
    val total  = flourGrams + water + salt + yeast
    val loaves = ceil(total.toFloat() / DEFAULT_LOAF_SIZE_GRAMS).toInt()
    return RecipeResult(flourGrams, water, salt, yeast, total, loaves)
}
```

Constants: `SALT_PERCENT = 2.0f`, `YEAST_PERCENT = 1.0f`, `DEFAULT_LOAF_SIZE_GRAMS = 450`, `HYDRATION_MIN = 45f`, `HYDRATION_MAX = 100f`, `HYDRATION_DEFAULT = 65f`, `FLOUR_MIN_GRAMS = 1`, `FLOUR_MAX_GRAMS = 99_999`.

---

## Component Breakdown

| Component | Responsibility | Key Inputs |
|---|---|---|
| `BakersMathScreen` | Root; collects state, passes to children via `LazyColumn` | `UiState`, `(BakersMathEvent)->Unit` |
| `FlourInputSection` | Large numeric input; filters non-digits; emits `FlourChanged` | `flourInput`, `isError`, `onFlourChanged` |
| `HydrationSlider` | Continuous slider 45–100%; shows current %; emits `HydrationChanged` | `hydrationPercent`, `onHydrationChanged` |
| `HydrationPresets` | Four `FilterChip`s; highlights active preset; emits `PresetSelected` | `activePreset`, `onPresetSelected` |
| `IngredientRow` | One ingredient line: dot, name, optional subtitle, %, grams | `name`, `colorDot`, `percent`, `grams`, `subtitle?` |
| `IngredientsList` | Maps `RecipeResult` to four `IngredientRow`s; shows "—" when null | `recipe?`, `hydrationPercent`, `yeastType` |
| `TotalDoughCard` | Prominent total weight + loaf estimate string | `recipe?` |
| `ThemeToggle` | Sun/moon `IconButton`; emits `ThemeToggled` | `isDarkMode`, `onToggle` |

`FlourInputSection` uses `BasicTextField` (not `OutlinedTextField`) for full visual control. `KeyboardOptions(keyboardType = KeyboardType.Number)` is set. Non-digit characters are filtered before emitting.

---

## Data Flow

**Flour input:** keystroke → `FlourChanged` event → ViewModel validates/parses/clamps → recalculates recipe → emits new `UiState` → affected composables recompose.

**Slider drag:** `HydrationChanged(74.3f)` → ViewModel sets hydration, checks preset match (±0.1f tolerance), recalculates → new state.

**Preset tap:** `PresetSelected(HEARTH)` → ViewModel sets `hydrationPercent = 75f`, `activePreset = HEARTH`, recalculates → slider value is state-driven (no bidirectional binding issues).

**Theme toggle:** `ThemeToggled` → ViewModel inverts `isDarkMode` in state immediately (optimistic) → `viewModelScope.launch { themeRepository.setDarkMode(newValue) }` persists asynchronously.

**App startup:** `MainActivity.onCreate` reads theme from DataStore via `runBlocking` (single-key read, acceptable) → passes `isDarkMode` to ViewModel factory → `_uiState` initialized with correct theme → `setContent` called. No theme flash.

---

## Theming — Color Tokens

**Light:** `backgroundPrimary = #F5ECD7` (warm cream), `backgroundCard = #EDE0C4`, `textPrimary = #3B1F0A` (deep espresso), `textSecondary = #7A5230`, `accent = #C07D3A` (golden brown).

**Dark:** `backgroundPrimary = #1E1108` (very dark brown), `backgroundCard = #2D1A0A`, `textPrimary = #F5ECD7` (cream), `textSecondary = #D4B896`, `accent = #D4913F`.

Ingredient dots: flour = `#F0D9A8` (pale wheat), water = `#7BB3D4` (muted blue), salt = `#D4CFC9` (light grey), yeast = `#C8A96A` (warm tan).

---

## Dependencies (additions to base Compose project)

| Artifact | Version | Purpose |
|---|---|---|
| `androidx.datastore:datastore-preferences` | 1.1.1 | Theme persistence |
| `app.cash.turbine:turbine` (test) | 1.2.0 | StateFlow testing |
| Ktlint Gradle plugin | 12.1.2 | Code style enforcement |

No networking, no image loading, no DI framework.

---

## Security Considerations

- No `INTERNET` permission in manifest. Network calls are impossible.
- No sensitive data. DataStore holds one boolean.
- Flour field is numeric-only — no injection surface.
- No WebView, no external content, no permissions requested.
- Compliance: not applicable (no data collection).

---

## NFR Approach

**Performance:** calculations are sub-millisecond pure arithmetic. Recomposition scope is narrow — only `IngredientsList` and `TotalDoughCard` recompose on value change.

**Offline:** no network dependency by design.

**Accessibility:** all elements have `contentDescription`. Slider exposes its value to a11y services. Touch targets ≥ 48dp. Font sizes in `sp` (respects user font scale).

**Kitchen use:** `FLAG_KEEP_SCREEN_ON` set in `MainActivity` — screen stays on while the app is in the foreground.

**One-handed:** flour input at top, slider+chips in middle, totals at bottom. Full screen is scrollable.

---

## Residual Open Questions (post-v1)

1. Custom typeface: system font used in v1. A serif face would reinforce the craft aesthetic; requires a licensed asset and a design decision.
2. Haptic feedback: deferred to nice-to-have ticket (TICKET-13).
3. Share/copy recipe: deferred to nice-to-have ticket (TICKET-14).
4. Custom salt/yeast %: v2.
5. Yeast type selector: v2. Enum is already in the model.
