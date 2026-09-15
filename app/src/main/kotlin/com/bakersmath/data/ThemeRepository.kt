package com.bakersmath.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class ThemeRepository(private val dataStore: DataStore<Preferences>) {
    private val darkModeKey = booleanPreferencesKey("dark_mode")

    suspend fun getDarkMode(): Boolean = dataStore.data.first()[darkModeKey] ?: false

    suspend fun setDarkMode(dark: Boolean) {
        dataStore.edit { prefs -> prefs[darkModeKey] = dark }
    }
}
