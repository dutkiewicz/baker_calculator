# Baker's Math — Android App

## Description
Single-screen Android calculator for bread baking ratios (baker's percentages). User inputs flour weight; app instantly calculates all ingredient weights based on standard baking ratios, with adjustable hydration.

## Goals
1. User can calculate all ingredient weights in under 10 seconds, start to finish.
2. Hydration adjustment (slider + presets) updates all values in real time with no lag.
3. App is usable in a kitchen — large readable numbers, one-handed interaction, no account or internet required.
4. Visual design reflects the craft: warm bread-tone palette, feels intentional and premium.
5. Zero learning curve — no tutorial needed for a home baker to use it on first launch.

---

## Must-Haves

- **Flour input**: Numeric input field (grams). Large, prominent display. This is the base — all other values derive from it.
- **Real-time calculation**: All ingredient weights update instantly as flour value or hydration changes.
- **Hydration slider**: Continuous slider to adjust water percentage. Default 65%. Range: 50%–90%.
- **Hydration presets**: Four quick-select chips below the slider:
  - 60% · Stiff
  - 65% · Standard *(default)*
  - 75% · Hearth
  - 85% · Ciabatta
  - Selecting a preset snaps slider to that value.
- **Ingredients breakdown**: Displays all four ingredients with percentage and calculated grams:
  | Ingredient | Default % |
  |---|---|
  | Flour | 100% (base) |
  | Water | 65% (adjustable) |
  | Salt | 2.0% (fixed) |
  | Yeast | 1.0% (fixed) |
- **Total dough weight**: Sum of all ingredients, shown prominently at bottom. Includes loaf estimate (e.g. "≈ 2 loaves at 450g").
- **Single screen**: Everything on one scrollable screen. No navigation.
- **Light/dark mode toggle**: Manual toggle (sun/moon icon), persists across sessions.
- **Bread-tone visual design**: Warm beige/cream backgrounds, dark brown typography, golden-brown accents. Match reference design.

---

## Nice-to-Haves

- **Yeast type selector**: Switch between instant dry / active dry / fresh yeast — each has a different weight multiplier (e.g. fresh yeast = ~3× instant). The subtitle "instant dry" in the design hints at this.
- **Custom salt/yeast %**: Allow advanced users to override the fixed percentages.
- **Loaf size selector**: Let user adjust the target loaf weight (currently hardcoded at 450g) to recalculate loaf count.
- **Share/copy recipe**: One-tap copy or share of the full ingredient list as plain text.
- **Haptic feedback**: Subtle vibration when slider snaps to a preset.

---

## Open Questions

1. **Hydration slider range**: Design suggests 60–85% visible via presets, but should the slider physically allow values outside that range (e.g. 50% for cracker dough, 100%+ for ciabatta extremes)?
2. **Yeast type in v1**: Should "instant dry" be a static label or a selector from day one? Affects yeast weight shown.
3. **Flour input limits**: Should there be a min/max gram value, or accept any positive number?
4. **Loaves estimate logic**: Is 450g the right default loaf size? Should it be adjustable in v1?
5. **Offline-only confirmed**: No sync, no accounts, no analytics — pure local app?

---

## User Stories

### Flour Input

**As a home baker, I want to type in my flour weight so that all other ingredient amounts are calculated for me automatically.**

Acceptance criteria:
- Given the app is open, when I tap the flour field and type a number, then all ingredient weights and the total dough weight update immediately.
- Given I type "500", then Water shows 325g (65%), Salt shows 10g (2.0%), Yeast shows 5g (1.0%), Total shows 840g.
- Given I clear the flour field, then all calculated values show "—" or "0g" (not a crash or error).

---

### Hydration Slider

**As a baker, I want to adjust water percentage with a slider so that I can dial in the exact hydration for my recipe style.**

Acceptance criteria:
- Given flour is entered, when I drag the hydration slider, then the Water weight and percentage update in real time as I drag.
- Given the slider is at 65%, when I drag it to 75%, then Water weight recalculates to flour × 0.75.
- Given I type flour weight after setting hydration, then hydration percentage stays at my chosen value.

---

### Hydration Presets

**As a baker, I want to tap a preset chip (Stiff / Standard / Hearth / Ciabatta) so that I can quickly jump to a known hydration level without dragging the slider.**

Acceptance criteria:
- Given I tap "75% · Hearth", then the slider snaps to 75% and Water weight updates immediately.
- Given "65% · Standard" is selected, when I drag the slider away from 65%, then no preset chip appears selected.
- Given I tap a preset, then that chip is visually highlighted (filled background); others are outlined.

---

### Ingredients Breakdown

**As a baker, I want to see all four ingredients listed with both their percentage and gram weight so that I can read off exact amounts to measure.**

Acceptance criteria:
- Given flour = 500g and hydration = 65%, then the ingredients list shows: Flour 100% 500g · Water 65% 325g · Salt 2.0% 10g · Yeast 1.0% 5g.
- Given I change flour to 1000g, then all gram weights double while percentages stay the same.
- Each ingredient row has a distinct color indicator (dot/swatch) matching the design palette.

---

### Total Dough & Loaf Estimate

**As a baker, I want to see the total dough weight and an estimated loaf count so that I know how much bread this batch will make.**

Acceptance criteria:
- Given flour = 500g, hydration = 65%, salt = 2%, yeast = 1%, then Total Dough shows 840g.
- Given total dough is 840g and loaf size is 450g, then the estimate reads "≈ 2 loaves at 450g".
- Given total dough changes (flour or hydration adjusted), then the loaf estimate updates in real time.

---

### Light / Dark Mode

**As a baker using my phone in a dim kitchen, I want to toggle dark mode so that the screen is comfortable to read in low light.**

Acceptance criteria:
- Given I tap the light/dark toggle icon, then the entire screen switches to the alternate color scheme.
- Given I toggle dark mode and close the app, when I reopen it, then dark mode is still active.
- Both modes use bread-tone palette: dark mode uses deep brown backgrounds with cream text; light mode uses cream/beige backgrounds with dark brown text.
