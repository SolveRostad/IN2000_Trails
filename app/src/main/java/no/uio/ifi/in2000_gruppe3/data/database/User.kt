package no.uio.ifi.in2000_gruppe3.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "isSelected")
    val isSelected: Int = 0,

    @ColumnInfo("distanceDone")
    val distanceDone: Double = 0.0,

    @ColumnInfo("hikesDone")
    val hikesDone: Int = 0,

    @ColumnInfo("history_hikes_done")
    val historyHikesDone: List<String> = emptyList()
)
