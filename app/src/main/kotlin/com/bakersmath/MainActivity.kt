package com.bakersmath

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bakersmath.data.ThemeRepository
import com.bakersmath.ui.BakersMathScreen
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val ComponentActivity.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val themeRepository = ThemeRepository(dataStore)
        val isDarkMode = runBlocking { themeRepository.getDarkMode() }

        val viewModel: com.bakersmath.viewmodel.BakersMathViewModel by viewModels {
            ViewModelFactory(isDarkMode)
        }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BreadTheme(isDarkMode = uiState.isDarkMode) {
                BakersMathScreen(viewModel = viewModel)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState
                .map { it.isDarkMode }
                .distinctUntilChanged()
                .drop(1)
                .collect { isDark -> themeRepository.setDarkMode(isDark) }
        }
    }
}
