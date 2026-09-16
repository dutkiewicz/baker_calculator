# Baker's Math — Developer Tickets

---

## TICKET-1: Project setup — Android module, Gradle, dependencies

**Type:** Chore | **Size:** S (half day) | **Depends on:** none

### Task
Create Android project with package `com.bakersmath`. Configure `gradle/libs.versions.toml`. Set `minSdk=26`, `targetSdk=35`. Add Compose BOM, Material3, lifecycle-viewmodel-compose, lifecycle-runtime-compose, DataStore Preferences, Turbine (test). Add Ktlint Gradle plugin. Stub `MainActivity.kt` with `setContent { Text("Baker's Math") }` to confirm build.

### Acceptance Criteria
- [ ] Project compiles and launches on emulator (API 26+)
- [ ] All dependency versions match tech spec
- [ ] `libs.versions.toml` used — no version literals in build files
- [ ] `./gradlew ktlintCheck` passes on stub code
- [ ] Package structure folders exist

### Notes
Start from "Empty Activity" template, delete generated XML/View code. Do not use "Compose Activity" template.

---

## TICKET-2: Domain layer — constants, enums, calculation function, unit tests

**Type:** Feature | **Size:** S (half day) | **Depends on:** TICKET-1

### Task
Create:
- `domain/Constants.kt` — `SALT_PERCENT`, `YEAST_PERCENT`, `DEFAULT_LOAF_SIZE_GRAMS`, `HYDRATION_MIN/MAX/DEFAULT`, `FLOUR_MIN/MAX_GRAMS`
- `domain/YeastType.kt` — enum with INSTANT_DRY (active), ACTIVE_DRY, FRESH (stubs)
- `domain/RecipeResult.kt` — data class per tech spec
- `domain/HydrationPreset.kt` — enum with four values
- `domain/RecipeCalculator.kt` — pure function `calculateIngredients(flourGrams: Int, hydrationPercent: Float): RecipeResult`
- `test/.../domain/RecipeCalculatorTest.kt`

### Acceptance Criteria
- [ ] flour=500, hydration=65f → water=325, salt=10, yeast=5, total=840, loaves=2
- [ ] flour=1000, hydration=65f → water=650, salt=20, yeast=10, total=1680, loaves=4
- [ ] flour=500, hydration=75f → water=375, total=895, loaves=2
- [ ] flour=400, hydration=65f → total=672, loaves=2 (ceil(672/450))
- [ ] flour=200, hydration=65f → total=336, estimatedLoaves=1, total < loafSizeGrams
- [ ] `./gradlew test` passes

### Notes
The "< 1 loaf" display string lives in `TotalDoughCard`, not the calculator. Calculator always returns `ceil(...)`.

---

## TICKET-3: ViewModel + UiState — state model and event handling

**Type:** Feature | **Size:** M (1–2 days) | **Depends on:** TICKET-2

### Task
Create:
- `viewmodel/UiState.kt` — data class per tech spec
- `viewmodel/BakersMathViewModel.kt` — ViewModel with `_uiState: MutableStateFlow<UiState>`, `onEvent(BakersMathEvent)` handler
  - Flour: strip non-digits, parse to `Int?`, clamp to `FLOUR_MIN..FLOUR_MAX`
  - Preset match: compare hydrationPercent against each preset's percent (exact float equality; drags won't produce exact values unless snapped)
  - Recalculates when `flourGrams != null`
- `BakersMathEvent` sealed interface — all four event types
- `ViewModelFactory` accepting initial `isDarkMode: Boolean`
- `test/.../viewmodel/BakersMathViewModelTest.kt` using Turbine

### Acceptance Criteria
- [ ] `FlourChanged("500")` → `uiState.recipe.totalGrams == 840`
- [ ] `FlourChanged("")` → `uiState.recipe == null`, no crash
- [ ] `FlourChanged("100000")` → `uiState.flourGrams == 99999`, `uiState.flourInputError == true`
- [ ] `HydrationChanged(65f)` → `uiState.activePreset == HydrationPreset.STANDARD`
- [ ] `HydrationChanged(70f)` → `uiState.activePreset == null`
- [ ] `PresetSelected(HEARTH)` → `uiState.hydrationPercent == 75f`, `uiState.activePreset == HEARTH`
- [ ] `ThemeToggled` twice → `isDarkMode` returns to original value
- [ ] All ViewModel tests pass

### Notes
ViewModel does not depend on DataStore directly — only receives initial `isDarkMode` from factory. DataStore in `ThemeRepository` (TICKET-5).

---

## TICKET-4: Theming — BreadColors, BreadTheme, CompositionLocal

**Type:** Feature | **Size:** S (half day) | **Depends on:** TICKET-1

### Task
Create:
- `ui/theme/BreadColors.kt` — `data class BreadColors(...)` with all fields from tech spec. `LightBreadColors` and `DarkBreadColors` val instances with hex values.
- `ui/theme/LocalBreadColors.kt` — `val LocalBreadColors = compositionLocalOf { LightBreadColors }`
- `ui/theme/BreadTypography.kt` — `val BreadTypography = Typography(...)` overriding `displayLarge` (57sp), `titleLarge` (22sp), `bodyMedium` (14sp) at minimum
- `ui/theme/BreadTheme.kt` — `@Composable fun BreadTheme(isDarkMode: Boolean, content: @Composable () -> Unit)` wrapping `MaterialTheme`, providing `LocalBreadColors`

Add `@Preview` for both light and dark modes.

### Acceptance Criteria
- [ ] Light preview: cream background, dark text
- [ ] Dark preview: dark brown background, cream text
- [ ] No color values appear outside `BreadColors.kt`
- [ ] `LocalBreadColors.current` accessible from any composable inside `BreadTheme`
- [ ] Compile and preview work without error

### Notes
Do not use `MaterialTheme.colorScheme` directly in components. All color references go through `LocalBreadColors.current`.

---

## TICKET-5: DataStore — theme persistence and app startup

**Type:** Feature | **Size:** S (half day) | **Depends on:** TICKET-3, TICKET-4

### Task
Create `data/ThemeRepository.kt` — wraps DataStore. Exposes `suspend fun setDarkMode(dark: Boolean)` and `suspend fun getDarkMode(): Boolean`. Key: `PreferencesKeys.booleanKey("dark_mode")`. Default: `false`.

Update `MainActivity.kt`:
- Create `DataStore` instance via `preferencesDataStore` delegate
- In `onCreate`, read initial theme: `runBlocking { themeRepository.getDarkMode() }`
- Instantiate `BakersMathViewModel` via factory with `isDarkMode`
- In `ThemeToggled` handler: `viewModelScope.launch { themeRepository.setDarkMode(newValue) }`

### Acceptance Criteria
- [ ] Dark mode ON → kill → reopen → dark mode ON
- [ ] Dark mode toggled OFF → kill → reopen → dark mode OFF
- [ ] DataStore write is async — UI does not wait
- [ ] No `runBlocking` except the single startup read in `onCreate`
- [ ] `ThemeRepository` is unit-testable (inject DataStore via constructor)

### Notes
The single `runBlocking` on startup is intentional (per tech spec). Reads one boolean — blocking time is unmeasurable. Do not replace with splash screen or loading state.

---

## TICKET-6: FlourInputSection composable

**Type:** Feature | **Size:** S (half day) | **Depends on:** TICKET-4

### Task
Create `ui/components/FlourInputSection.kt`:
- `BasicTextField` (not `OutlinedTextField`) for full visual control
- `KeyboardOptions(keyboardType = KeyboardType.Number)`, `ImeAction.Done`
- Filter non-digit characters in `onValueChange` before calling `onFlourChanged`
- Value displayed at `displayLarge` typography, `textPrimary` color
- Placeholder "0" or "Enter flour (g)" via custom `decorationBox` when empty
- "g" unit suffix inline with the number
- `isError = true` shows small error hint "Max 99,999g" in `textSecondary`
- Signature: `fun FlourInputSection(flourInput: String, isError: Boolean, onFlourChanged: (String) -> Unit)`
- `@Preview` in light and dark mode

### Acceptance Criteria
- [ ] Tap opens numeric keyboard
- [ ] Non-digits (letters, symbols, decimal point) cannot be entered
- [ ] Empty field shows placeholder, no crash
- [ ] `isError = true` shows error hint
- [ ] "g" suffix always visible
- [ ] Preview renders in both themes

---

## TICKET-7: HydrationSlider and HydrationPresets composables

**Type:** Feature | **Size:** M (1–2 days) | **Depends on:** TICKET-4

### Task
Create `ui/components/HydrationSlider.kt`:
- Material3 `Slider`, range `HYDRATION_MIN..HYDRATION_MAX` (45f–100f), continuous
- Current percentage label: `"${hydrationPercent.roundToInt()}%"`
- Thumb and active track: `accent` color from `LocalBreadColors`
- Signature: `fun HydrationSlider(hydrationPercent: Float, onHydrationChanged: (Float) -> Unit)`

Create `ui/components/HydrationPresets.kt`:
- Four `FilterChip`s in `Row(Arrangement.spacedBy(8.dp))`
- Label: `"${preset.percent.roundToInt()}% · ${preset.style}"` (e.g. "65% · Standard")
- `selected = (activePreset == preset)`
- Selected: filled `chipSelectedBackground`, `chipSelectedText`; Unselected: outlined `chipOutlineColor`, transparent bg
- Iterate `HydrationPreset.entries` — no hardcoded four calls
- Signature: `fun HydrationPresets(activePreset: HydrationPreset?, onPresetSelected: (HydrationPreset) -> Unit)`
- `@Preview` showing STANDARD selected

### Acceptance Criteria
- [ ] Slider moves 45%–100% continuously
- [ ] Thumb is golden-brown (accent) in light mode
- [ ] All four chips render with correct labels
- [ ] Active chip filled; others outlined
- [ ] `activePreset = null` → no chip filled
- [ ] Preview renders correctly

### Notes
`FilterChip` `colors` needs explicit overrides via `FilterChipDefaults.filterChipColors(...)` to use `BreadColors` tokens.

---

## TICKET-8: IngredientRow and IngredientsList composables

**Type:** Feature | **Size:** S (half day) | **Depends on:** TICKET-4

### Task
Create `ui/components/IngredientRow.kt`:
- `Row(Arrangement.SpaceBetween)`: left = 12dp circle dot + ingredient name + optional subtitle; right = percentage + gram weight
- Gram weight: `titleLarge` typography; Percentage: `bodyMedium`, `textSecondary`
- Dot colors from `BreadColors` tokens: `flourDot`, `waterDot`, `saltDot`, `yeastDot`
- Signature: `fun IngredientRow(name: String, colorDot: Color, percentLabel: String, gramsLabel: String, subtitle: String? = null)`

Create `ui/components/IngredientsList.kt`:
- Four `IngredientRow`s in `Column` with `Divider`s between rows
- Maps `RecipeResult?` → display values; shows "—" when null
- Signature: `fun IngredientsList(recipe: RecipeResult?, hydrationPercent: Float, yeastType: YeastType)`
- `@Preview` for null and non-null `recipe`

### Acceptance Criteria
- [ ] All four ingredients render with correct dot colors per tech spec palette
- [ ] `recipe == null` → all gram labels show "—"
- [ ] Yeast row shows "instant dry" subtitle below the name
- [ ] Gram weights use `titleLarge` scale
- [ ] Dividers visible in both modes

### Notes
Format grams as integers — `roundToInt()` is applied in the calculator, not the composable.

---

## TICKET-9: TotalDoughCard composable

**Type:** Feature | **Size:** XS (<1h) | **Depends on:** TICKET-4

### Task
Create `ui/components/TotalDoughCard.kt`:
- `Card` using `backgroundCard` color, 12dp rounded corners
- Total weight label "Total Dough" in `textSecondary`, `bodyMedium`
- Total weight value in `textPrimary`, `headlineLarge`
- Loaf estimate string logic:
  - `recipe == null` → "—"
  - `recipe.totalGrams < recipe.loafSizeGrams` → "< 1 loaf at ${loafSizeGrams}g"
  - Otherwise → "≈ ${recipe.estimatedLoaves} loaves at ${recipe.loafSizeGrams}g"
- Signature: `fun TotalDoughCard(recipe: RecipeResult?)`
- `@Preview` for null and non-null states

### Acceptance Criteria
- [ ] total=840, estimatedLoaves=2, loafSize=450 → "≈ 2 loaves at 450g"
- [ ] total=300 → "< 1 loaf at 450g"
- [ ] `recipe == null` → "—" for both weight and estimate
- [ ] Total gram value is most visually prominent text on card
- [ ] Card uses `backgroundCard` (distinct from `backgroundPrimary`)

---

## TICKET-10: ThemeToggle composable and MainActivity wiring

**Type:** Feature | **Size:** S (half day) | **Depends on:** TICKET-5, TICKET-4

### Task
Create `ui/components/ThemeToggle.kt`:
- `IconButton`: sun icon when `isDarkMode=false`, moon icon when `isDarkMode=true`
- Icon tint: `accent` color
- `contentDescription`: announces the action ("Switch to dark mode" / "Switch to light mode")
- Signature: `fun ThemeToggle(isDarkMode: Boolean, onToggle: () -> Unit)`

Update `MainActivity.kt`:
- Observe `uiState.isDarkMode`
- Pass to `BreadTheme(isDarkMode = uiState.isDarkMode)`
- Toggle sits top-right of screen (not in TopAppBar)

### Acceptance Criteria
- [ ] Sun tap → dark mode; moon tap → light mode
- [ ] Icon updates immediately on tap
- [ ] Entire screen color scheme switches on toggle
- [ ] Reopening app after toggle shows correct mode (DataStore from TICKET-5)
- [ ] `contentDescription` correctly announces action in either state

---

## TICKET-11: BakersMathScreen — assemble all components

**Type:** Feature | **Size:** M (1–2 days) | **Depends on:** TICKET-3, TICKET-6, TICKET-7, TICKET-8, TICKET-9, TICKET-10

### Task
Create `ui/BakersMathScreen.kt`:
- `fun BakersMathScreen(viewModel: BakersMathViewModel = viewModel())`
- `val uiState by viewModel.uiState.collectAsStateWithLifecycle()`
- `LazyColumn` with `contentPadding` for system bars. Order:
  1. ThemeToggle (top-right header row)
  2. FlourInputSection
  3. HydrationSlider
  4. HydrationPresets
  5. IngredientsList
  6. TotalDoughCard
- Wire all `onXxx` callbacks to `viewModel.onEvent(...)`

Update `MainActivity.kt`:
- Enable edge-to-edge via `enableEdgeToEdge()` and `WindowInsetsCompat` padding
- Host `BakersMathScreen()` inside `BreadTheme`

### Acceptance Criteria
- [ ] Type "500" → all four ingredient rows update → total shows "840g"
- [ ] Drag slider to 75% → Water row updates → no preset chip highlighted
- [ ] Tap "75% · Hearth" → slider moves to 75% → HEARTH chip highlighted
- [ ] Clear flour field → all gram values show "—", no crash
- [ ] Toggle switches theme and persists across restart
- [ ] Screen scrollable on small phones (no content clipped)
- [ ] Edge-to-edge content does not overlap system bars

### Notes
Use `collectAsStateWithLifecycle()` (requires `lifecycle-runtime-compose`). Do not use `collectAsState()`.

---

## TICKET-12: Keep-screen-on and accessibility polish

**Type:** Chore | **Size:** XS (<1h) | **Depends on:** TICKET-11

### Task
In `MainActivity.kt`:
```kotlin
window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
```

Audit all composables for missing `contentDescription`:
- `HydrationSlider`: `Modifier.semantics { contentDescription = "Hydration slider, ${hydrationPercent.roundToInt()} percent" }`
- `FlourInputSection`: `Modifier.semantics { contentDescription = "Flour weight input" }`
- `IngredientRow`: `Row(modifier = Modifier.semantics(mergeDescendants = true) {})`
- Verify all `IconButton`s have `contentDescription`

Verify minimum 48dp touch targets on all tappable elements.

### Acceptance Criteria
- [ ] Screen does not auto-dim or lock while app is in foreground
- [ ] TalkBack reads flour input, slider, each preset chip, each ingredient row, and toggle meaningfully
- [ ] No touch target smaller than 48dp

---

## TICKET-13 (Nice-to-have): Haptic feedback on preset snap

**Type:** Feature | **Size:** XS (<1h) | **Depends on:** TICKET-7

### Task
In `HydrationPresets.kt`, inside each chip's `onClick`:
```kotlin
val haptic = LocalHapticFeedback.current
haptic.performHapticFeedback(HapticFeedbackType.LongPress)
```

### Acceptance Criteria
- [ ] Tapping any preset chip produces a haptic pulse on a physical device
- [ ] No crash or warning on emulators (silently ignored)
- [ ] Haptic does NOT fire when slider moves value to a preset — only on direct chip tap

### Notes
Test on physical device. Emulators produce no haptic output.

---

## TICKET-14 (Nice-to-have): Share / copy recipe as plain text

**Type:** Feature | **Size:** S (half day) | **Depends on:** TICKET-11

### Task
Add share `IconButton` (`Icons.Rounded.Share`) to `TotalDoughCard` or bottom of screen (confirm placement with PM before implementing).

On tap, build plain-text recipe string:
```
Baker's Math Recipe
-------------------
Flour:  500g  (100%)
Water:  325g   (65%)
Salt:    10g  (2.0%)
Yeast:    5g  (1.0%)
-------------------
Total:  840g
≈ 2 loaves at 450g
```

Trigger share sheet via `Intent.ACTION_SEND` with `type = "text/plain"`. Also copy to clipboard via `ClipboardManager`.

### Acceptance Criteria
- [ ] Valid recipe displayed → share button tap → Android share sheet appears with formatted text
- [ ] Share button disabled/hidden when `recipe == null`
- [ ] Formatted text readable in plain-text context (notes app, SMS)
- [ ] No crash when share sheet is dismissed without sharing

### Notes
Use `LocalContext.current` to get `Context` for `startActivity`. No new permissions required.
