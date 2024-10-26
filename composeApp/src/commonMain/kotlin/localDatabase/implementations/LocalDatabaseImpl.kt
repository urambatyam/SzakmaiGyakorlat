package localDatabase.implementations

import androidx.room.Database
import androidx.room.RoomDatabase
import localDatabase.daos.DataDao
import localDatabase.models.Data

@Database(
    entities = [Data::class],
    version = 2
)
abstract class LocalDatabaseImpl: RoomDatabase() {
    abstract fun dataDao(): DataDao
}