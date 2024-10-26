package dataStorePreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class AppPreferencesImpl(
    private val dataStore: DataStore<Preferences>
): AppPreferences {
    private companion object {
        private const val PREFS_TAG_KEY = "AppPreferences"
        private const val FOLDERPATH = "folder"
    }

    private val folderPathKey = stringPreferencesKey("$PREFS_TAG_KEY$FOLDERPATH")

    override suspend fun getFolderPath() = dataStore.data.map { preferences ->
        preferences[folderPathKey]
    }.first()

    override suspend fun setFolderPath(path: String) = dataStore.edit { preferences ->
        preferences[folderPathKey] = path
    }
}