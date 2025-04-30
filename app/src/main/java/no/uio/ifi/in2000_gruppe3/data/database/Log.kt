package no.uio.ifi.in2000_gruppe3.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "log_table",
    primaryKeys = ["username", "hike_id"],
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = arrayOf("username"),
            childColumns = arrayOf("username"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)

data class Log (
    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo("hike_id")
    val logHikeId: Int,

    @ColumnInfo("times_walked")
    val timesWalked: Int = 0,

    @ColumnInfo("notes")
    val notes: String = ""
)