package no.uio.ifi.in2000_gruppe3.data.user

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import no.uio.ifi.in2000_gruppe3.data.database.Profile
import no.uio.ifi.in2000_gruppe3.data.database.ProfileDao
import no.uio.ifi.in2000_gruppe3.data.database.ProfileFavoritesDatabase

class ProfileRepository private constructor(private val profileDao: ProfileDao) {

    suspend fun addUser(profile: Profile) {
        profileDao.insertUser(profile)
    }

    suspend fun deleteUser(profile: Profile) {
        profileDao.deleteUser(profile)
    }

    suspend fun selectUser(username: String) {
        unselectUser()
        profileDao.selectUser(username)
    }

    suspend fun unselectUser() {
        profileDao.unselectUser()
    }

    suspend fun getSelectedUser(): Profile {
        val selectedUser = profileDao.getSelectedUser()
        return selectedUser ?: profileDao.getDefaultUser()?.also {
            profileDao.selectUser(it.username)
        } ?: throw IllegalStateException("Default user not found in the database.")
    }

    suspend fun getAllUsers(): List<Profile> {
        return profileDao.getAllUsers()
    }

    // Dene funksjonen er kun til testing.
    suspend fun clearAllUsers() {
        profileDao.clearAllUsers()
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null

        fun getInstance(context: Context, scope: CoroutineScope = CoroutineScope(SupervisorJob())): ProfileRepository {
            return INSTANCE ?: synchronized(this) {
                val database = ProfileFavoritesDatabase.getDatabase(context, scope)
                val instance = ProfileRepository(database.userDao())
                INSTANCE = instance
                instance
            }
        }
    }
}