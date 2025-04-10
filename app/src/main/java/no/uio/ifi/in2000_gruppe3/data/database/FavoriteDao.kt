package no.uio.ifi.in2000_gruppe3.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Insert
    suspend fun saveFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

//    @Query("SELECT * FROM favorite_table WHERE username LIKE :username AND hike_id LIKE :hikeId")
//    suspend fun getTimesWalked(username: String, hikeId: String): Int
//
//    @Query("UPDATE favorite_table SET times_walked = times_walked + 1 WHERE username LIKE :username AND hike_id LIKE :hikeId")
//    suspend fun incrementTimesWalked(username: String, hikeId: String)

    @Query("SELECT * FROM favorite_table WHERE username LIKE :username")
    suspend fun getAllFavorites(username: String): List<Favorite>

    //@Query("UPDATE favorite_table SET isSelected = 1 WHERE username LIKE :username AND hike_id LIKE :hikeId")
    //fun selectFavorite(username: String, hikeId: String)

    //@Query("SELECT * FROM favorite_table INNER JOIN user_table ON favorite_table.username = user_table.username WHERE favorite_table.isSelected AND user_table.isSelected")
    //fun getSelectedFavorite(): Favorite

    //@Query("UPDATE favorite_table SET isSelected = 0 WHERE username LIKE :username")
    //fun unselectFavorite(username: String)
}