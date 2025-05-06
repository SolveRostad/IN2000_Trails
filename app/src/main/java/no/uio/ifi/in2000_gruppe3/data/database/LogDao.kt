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

    @Query("SELECT hike_id FROM log_table WHERE username LIKE :username")
    suspend fun getAllLogs(username: String): List<Int>

    @Query("UPDATE log_table SET times_walked = times_walked + :adjustTimesWalked WHERE username LIKE :username AND hike_id = :hikeId")
    suspend fun adjustTimesWalked (username: String, hikeId: Int, adjustTimesWalked: Int)

    @Query("UPDATE log_table SET notes = :notes WHERE username LIKE :username AND hike_id = :hikeId")
    suspend fun addNotesToLog(username: String, hikeId: Int, notes: String)

    @Query("SELECT notes FROM log_table WHERE username LIKE :username AND hike_id = :hikeId")
    suspend fun getNotesForHike(username: String, hikeId: Int ): String

    @Query("SELECT SUM(times_walked) FROM log_table WHERE username LIKE :username")
    suspend fun getTotalTimesWalked(username: String): Int

    @Query("SELECT times_walked FROM log_table WHERE username LIKE :username AND hike_id = :hikeId")
    suspend fun getTimesWalkedForHike(username: String, hikeId: Int): Int
}