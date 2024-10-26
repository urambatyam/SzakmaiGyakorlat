package localDatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "data",
    indices = [Index(value = ["date"])],
)
data class Data(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 0,

    val thermometerId: String,

    @ColumnInfo(defaultValue = "0")
    val date: String,

    @ColumnInfo(defaultValue = "0")
    val time: String,

    val temperature: Int
){
    override fun toString(): String {
        return "id: $id thermometerId: $thermometerId date: $date time: $time id: $temperature"
    }
}
