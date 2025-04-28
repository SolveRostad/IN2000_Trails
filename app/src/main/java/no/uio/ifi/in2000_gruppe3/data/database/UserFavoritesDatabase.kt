package no.uio.ifi.in2000_gruppe3.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

        private class DatabaseCallBack(
            private val database: UserFavoritesDatabase,
            private val scope: CoroutineScope
        ) : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                scope.launch {
                    populateDatabase()
                }
            }

            private suspend fun populateDatabase() {
                val userDao = database.userDao()

                val defaultUser = User("defaultUser", isSelected = 1)

                userDao.insertUser(defaultUser)
                Log.d("DatabaseCallBack", "Default user inserted: $defaultUser")
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): UserFavoritesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserFavoritesDatabase::class.java,
                    "user_favorites_database"
                )
                    .build()

                INSTANCE = instance

                // Now that instance is set, insert the default user synchronously
                runBlocking {
                    val userDao = instance.userDao()
                    // Check if default user exists first to avoid duplicates
                    if (userDao.getDefaultUser() == null) {
                        val defaultUser = User("defaultUser", isSelected = 1)
                        userDao.insertUser(defaultUser)
                        Log.d("DatabaseCallBack", "Default user inserted: $defaultUser")
                    }
                }

                instance
            }
        }
    }
}