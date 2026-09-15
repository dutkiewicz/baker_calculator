package com.bakersmath.data

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ThemeRepositoryTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private fun dataStore() =
        PreferenceDataStoreFactory.create(
            produceFile = { File(temporaryFolder.root, "theme.preferences_pb") },
        )

    @Test
    fun `defaults to light mode when nothing is stored`() =
        runTest {
            val repository = ThemeRepository(dataStore())
            assertFalse(repository.getDarkMode())
        }

    @Test
    fun `round trips dark mode through the data store`() =
        runTest {
            val repository = ThemeRepository(dataStore())

            repository.setDarkMode(true)
            assertTrue(repository.getDarkMode())

            repository.setDarkMode(false)
            assertFalse(repository.getDarkMode())
        }

    @Test
    fun `persisted value is visible to another repository over the same data store`() =
        runTest {
            val dataStore = dataStore()
            ThemeRepository(dataStore).setDarkMode(true)

            assertTrue(ThemeRepository(dataStore).getDarkMode())
        }
}
