package no.uio.ifi.in2000_gruppe3.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(username: User)

    @Query("SELECT * FROM user_table")
    suspend fun getAllUsers(): List<User>

    @Query("UPDATE user_table SET isSelected = 1 WHERE username LIKE :username")
    suspend fun selectUser(username: String)

    @Query("SELECT * FROM user_table WHERE isSelected = 1 LIMIT 1")
    suspend fun getSelectedUser(): User?

    @Query("SELECT * FROM user_table WHERE username = 'defaultUser' LIMIT 1")
    suspend fun getDefaultUser(): User?

    @Query("UPDATE user_table SET isSelected = 0")
    suspend fun unselectUser()

    @Query("DELETE from user_table")
    suspend fun clearAllUsers()

    @Query("UPDATE user_table SET distanceDone = distanceDone + :distance, hikesDone = hikesDone + 1 WHERE username LIKE :username")
    fun addDataTo(username: String, distance: Double)

    @Query("UPDATE user_table SET history_hikes_done = :historyList WHERE username = :username")
    suspend fun updateUserHistory(username: String, historyList: List<String>)

    @Transaction
    suspend fun addHikeToHistory(hikeId: String) {
        val user = getSelectedUser() ?: return
        val updatedHistory = user.historyHikesDone + hikeId

        updateUserHistory(user.username, updatedHistory)

    }
}