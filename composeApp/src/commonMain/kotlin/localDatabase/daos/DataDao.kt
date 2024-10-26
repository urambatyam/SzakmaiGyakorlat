package localDatabase.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import localDatabase.models.Data


@Dao
interface DataDao {
    @Query("SELECT * FROM data")
    fun getAll(): Flow<List<Data>>

    @Query("SELECT DISTINCT date FROM data")
    fun getAllDates(): Flow<List<String>>

    @Query("SELECT DISTINCT thermometerId FROM data")
    fun getAllThermometerId(): Flow<List<String>>

    @Upsert
    suspend fun upsert(data: Data)

    @Delete
    suspend fun delete(data: Data)
}