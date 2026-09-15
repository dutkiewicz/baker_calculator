package com.bakersmath.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bakersmath.domain.HydrationPreset
import com.bakersmath.domain.RecipeResult
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
import com.bakersmath.viewmodel.UiState

@Composable
fun BakersMathScreen(viewModel: BakersMathViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BakersMathScreenContent(
        uiState = uiState,
        onFlourChanged = { viewModel.onEvent(BakersMathEvent.FlourChanged(it)) },
        onHydrationChanged = { viewModel.onEvent(BakersMathEvent.HydrationChanged(it)) },
        onPresetSelected = { viewModel.onEvent(BakersMathEvent.PresetSelected(it)) },
        onThemeToggled = { viewModel.onEvent(BakersMathEvent.ThemeToggled) },
        onShare = { recipe -> shareRecipe(context, buildRecipeShareText(recipe, uiState.hydrationPercent)) },
    )
}

/**
 * Stateless screen. Kept free of the ViewModel and DataStore so it can be
 * rendered directly by the Compose Preview Screenshot Testing source set.
 *
 * Layout contract: the content fills at least the viewport and scrolls when it
 * does not fit (small phones, large font scale, or the keyboard open). When it
 * does fit, the results block is pushed to the bottom of the screen.
 */
@Composable
fun BakersMathScreenContent(
    uiState: UiState,
    onFlourChanged: (String) -> Unit,
    onHydrationChanged: (Float) -> Unit,
    onPresetSelected: (HydrationPreset) -> Unit,
    onThemeToggled: () -> Unit,
    onShare: (RecipeResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current

    BoxWithConstraints(
        modifier = modifier.fillMaxSize().background(colors.backgroundPrimary),
    ) {
        val viewportMinHeight = maxHeight

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier =
                    Modifier
                        .widthIn(max = CONTENT_MAX_WIDTH)
                        .fillMaxWidth()
                        .heightIn(min = viewportMinHeight)
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        ThemeToggle(
                            isDarkMode = uiState.isDarkMode,
                            onToggle = onThemeToggled,
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    FlourInputSection(
                        flourInput = uiState.flourInput,
                        isError = uiState.flourInputError,
                        onFlourChanged = onFlourChanged,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(16.dp))

                    HydrationSlider(
                        hydrationPercent = uiState.hydrationPercent,
                        onHydrationChanged = onHydrationChanged,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(16.dp))

                    HydrationPresets(
                        activePreset = uiState.activePreset,
                        onPresetSelected = onPresetSelected,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.height(24.dp))

                    IngredientsList(
                        recipe = uiState.recipe,
                        hydrationPercent = uiState.hydrationPercent,
                        yeastType = YeastType.INSTANT_DRY,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(16.dp))

                    TotalDoughCard(
                        recipe = uiState.recipe,
                        onShare = uiState.recipe?.let { recipe -> { onShare(recipe) } },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

private val CONTENT_MAX_WIDTH = 600.dp

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
