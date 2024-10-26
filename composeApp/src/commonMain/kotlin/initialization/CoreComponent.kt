package initialization

import dataStorePreferences.AppPreferences
import localDatabase.daos.DataDao


interface CoreComponent : CoroutinesComponent {
    val appPreferences: AppPreferences
    val localDatabase: DataDao
}