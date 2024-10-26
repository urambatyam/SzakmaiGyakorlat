package dataStorePreferences

import androidx.datastore.preferences.core.Preferences

interface AppPreferences {
    suspend fun getFolderPath(): String?
    suspend fun setFolderPath(path: String): Preferences
}