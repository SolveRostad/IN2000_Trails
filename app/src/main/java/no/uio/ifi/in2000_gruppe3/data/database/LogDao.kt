package no.uio.ifi.in2000_gruppe3.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LogDao {
    @Insert
    suspend fun saveLog(log: Log)

    @Delete
    suspend fun deleteLog(log: Log)

    @Query("SELECT * FROM log_table WHERE username LIKE :username")
    suspend fun getAllLogs(username: String): List<Log>

    @Query("UPDATE log_table SET times_walked = times_walked + :timesWalked WHERE username LIKE :username AND hike_id = :hikeId")
    suspend fun timesWalked (username: String, hikeId: Int, timesWalked: Double)

    @Query("UPDATE log_table SET notes = :notes WHERE username LIKE :username AND hike_id = :hikeId")
    suspend fun addNotesToLog(username: String, hikeId: Int, notes: String)
}