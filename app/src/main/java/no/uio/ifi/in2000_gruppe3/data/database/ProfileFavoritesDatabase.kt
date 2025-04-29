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

@Database(entities = [Favorite::class, Profile::class], version = 1)
@TypeConverters(Converter::class)
abstract class ProfileFavoritesDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: ProfileFavoritesDatabase? = null

        private class DatabaseCallBack(
            private val database: ProfileFavoritesDatabase,
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

                val defaultProfile = Profile("defaultUser", isSelected = 1)

                userDao.insertUser(defaultProfile)
                Log.d("DatabaseCallBack", "Default user inserted: $defaultProfile")
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): ProfileFavoritesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfileFavoritesDatabase::class.java,
                    "user_favorites_database"
                )
                    .build()

                INSTANCE = instance

                runBlocking {
                    val userDao = instance.userDao()
                    if (userDao.getDefaultUser() == null) {
                        val defaultProfile = Profile("defaultUser", isSelected = 1)
                        userDao.insertUser(defaultProfile)
                        Log.d("DatabaseCallBack", "Default user inserted: $defaultProfile")
                    }
                }

                instance
            }
        }
    }
}