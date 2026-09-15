package com.bakersmath.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bakersmath.domain.YeastType
import com.bakersmath.domain.buildRecipeShareText
import com.bakersmath.ui.components.FlourInputSection
import com.bakersmath.ui.components.HydrationPresets
import com.bakersmath.ui.components.HydrationSlider
import com.bakersmath.ui.components.IngredientsList
import com.bakersmath.ui.components.ThemeToggle
import com.bakersmath.ui.components.TotalDoughCard
import com.bakersmath.ui.theme.LocalBreadColors
import com.bakersmath.viewmodel.BakersMathEvent
import com.bakersmath.viewmodel.BakersMathViewModel

@Composable
fun BakersMathScreen(viewModel: BakersMathViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = LocalBreadColors.current
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(colors.backgroundPrimary)) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    ThemeToggle(
                        isDarkMode = uiState.isDarkMode,
                        onToggle = { viewModel.onEvent(BakersMathEvent.ThemeToggled) },
                    )
                }
            }

            item {
                FlourInputSection(
                    flourInput = uiState.flourInput,
                    isError = uiState.flourInputError,
                    onFlourChanged = { viewModel.onEvent(BakersMathEvent.FlourChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                HydrationSlider(
                    hydrationPercent = uiState.hydrationPercent,
                    onHydrationChanged = { viewModel.onEvent(BakersMathEvent.HydrationChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                HydrationPresets(
                    activePreset = uiState.activePreset,
                    onPresetSelected = { viewModel.onEvent(BakersMathEvent.PresetSelected(it)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                IngredientsList(
                    recipe = uiState.recipe,
                    hydrationPercent = uiState.hydrationPercent,
                    yeastType = YeastType.INSTANT_DRY,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                TotalDoughCard(
                    recipe = uiState.recipe,
                    onShare =
                        uiState.recipe?.let { recipe ->
                            {
                                val text = buildRecipeShareText(recipe, uiState.hydrationPercent)
                                shareRecipe(context, text)
                            }
                        },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private fun shareRecipe(
    context: Context,
    text: String,
) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Baker's Math Recipe", text))

    val intent =
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
    context.startActivity(Intent.createChooser(intent, "Share recipe"))
}
