package initialization

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dataStorePreferences.AppPreferences
import dataStorePreferences.AppPreferencesImpl
import dataStorePreferences.dataStorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import localDatabase.daos.DataDao


internal class CoreComponentImpl internal constructor(
    dao: DataDao
) : CoreComponent,
    CoroutinesComponent by CoroutinesComponentImpl.create() {

    private val dataStore: DataStore<Preferences> = dataStorePreferences(
        corruptionHandler = null,
        coroutineScope = applicationScope + Dispatchers.IO,
        migrations = emptyList()
    )

    override val appPreferences : AppPreferences = AppPreferencesImpl(dataStore)
    override val localDatabase: DataDao = dao
}