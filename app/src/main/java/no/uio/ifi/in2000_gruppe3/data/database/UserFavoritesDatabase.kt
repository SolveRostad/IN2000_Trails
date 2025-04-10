package no.uio.ifi.in2000_gruppe3.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/** Database layout:
 * User
 * PN: username: String
 *  username: String
 *  isSelected: Int
 *
 * Favorite
 * PN: [username, hikeId]
 * FN: username -> User
 *   username: User
 *   hikeId: String
 *   isSelected: Int
 *   personalHikeComment: String
 *
 * Achievement
 * PN: [username, hikesDone]
 * FN: username -> User
 *   username: User
 *   amountHikes: Int
 *   distanceDone: Double
 *   hikesDone: Int
 */

@Database(entities = [Favorite::class, User::class], version = 1)
@TypeConverters(Converter::class)
abstract class UserFavoritesDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserFavoritesDatabase? = null

        fun getDatabase(context: Context): UserFavoritesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserFavoritesDatabase::class.java,
                    "user_favorites_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}