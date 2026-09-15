# Baker's Math

A small Android app for bread baking. You type the flour weight, and the app
shows how much water, salt, and yeast you need. It uses baker's percentages.

Everything works offline. There is no account and no internet permission.

## Features

- Enter flour in grams. All other amounts update as you type.
- Water amount based on hydration. Slider range is 45% to 100%, default 65%.
- Three quick presets: 60% Stiff, 65% Standard, 75% Hearth.
- Shows all four ingredients with their percent and grams.
- Shows total dough weight and an estimate of how many 450g loaves it makes.
- Light and dark theme. The choice is saved between sessions.
- Share or copy the recipe as plain text.
- Small vibration when you tap a preset.
- One screen, no menus. Screen stays on while the app is open.

## How the math works

Flour is always 100%. The other amounts are a percent of the flour weight.

| Ingredient | Percent |
|---|---|
| Flour | 100% (base) |
| Water | 65% (you can change it) |
| Salt | 2.0% |
| Yeast | 1.0% |

Example: 500g flour at 65% gives 325g water, 10g salt, 5g yeast, and 840g total.

## Tech stack

- Kotlin, Jetpack Compose (Material 3)
- MVVM with a ViewModel and StateFlow
- DataStore Preferences for the theme setting only
- Gradle Kotlin DSL with a version catalog
- Min SDK 26, target and compile SDK 35
- Built with JDK 25 (Gradle toolchain)
- Tests: JUnit 4 and Turbine

## Project layout

```
app/src/main/kotlin/com/bakersmath/
  MainActivity.kt
  ui/           screen, components, theme
  viewmodel/    UiState, events, ViewModel
  domain/       calculator, constants, result, presets
  data/         ThemeRepository (DataStore)
app/src/test/kotlin/com/bakersmath/   unit tests
```

## Build and run

You need JDK 25 and the Android SDK.

```
./gradlew assembleDebug      # build a debug APK
./gradlew installDebug       # install on a device or emulator
```

## Checks

```
./gradlew testDebugUnitTest  # unit tests
./gradlew ktlintCheck        # code style
./gradlew lintDebug          # Android Lint
```

## CI and releases

- Pull requests run tests, lint, and a debug build.
- The Release workflow is started by hand. Enter a git tag, and it builds the
  app and attaches the APK to the GitHub Release for that tag.

