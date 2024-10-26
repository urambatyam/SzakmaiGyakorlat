package localDatabase

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import localDatabase.daos.DataDao
import localDatabase.implementations.LocalDatabaseImpl
import java.io.File

fun getDao():DataDao{
    val dbFile = File(System.getProperty("java.io.tmpdir"), "data.db")
    return Room.databaseBuilder<LocalDatabaseImpl>(
        name = dbFile.absolutePath,
    )
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
        .dataDao()
}