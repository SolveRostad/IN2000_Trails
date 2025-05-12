package no.uio.ifi.in2000_gruppe3.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import no.uio.ifi.in2000_gruppe3.data.database.Activity as LogEntity

/** Database layout:
 * Profile
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
 * Activity
 * PN: [username, hikeId]
 * FN: username -> Profile
 *   username: String
 *   hikeId: Int
 *   adjustTimesWalked: Int
 *   notes: String
 */

@Database(entities = [LogEntity::class, Favorite::class, Profile::class], version = 3)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun profileDao(): ProfileDao
    abstract fun logDao(): ActivityDao

    companion object {
        @Volatile
        private var INSTANCE: ProfileDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ProfileDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfileDatabase::class.java,
                    "Profile_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            scope.launch {
                                INSTANCE?.let { database ->
                                    val profileDao = database.profileDao()
                                    if (profileDao.getDefaultUser() == null) {
                                        val defaultProfile = Profile("defaultUser", isSelected = 1)
                                        profileDao.insertUser(defaultProfile)
                                        Log.d("DatabaseCallBack", "Default profile inserted: $defaultProfile")
                                    }
                                }
                            }
                        }
                    })
                    .setQueryCallback({ sqlQuery, bindArgs ->
                        Log.d("RoomQuery", "SQL: $sqlQuery, Args: $bindArgs")
                    }, Executors.newSingleThreadExecutor())
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}