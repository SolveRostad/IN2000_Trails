package no.uio.ifi.in2000_gruppe3.data.user

import no.uio.ifi.in2000_gruppe3.data.database.User
import no.uio.ifi.in2000_gruppe3.data.database.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun addUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun selectUser(username: String) {
        unselectUser()
        userDao.selectUser(username)
    }

    suspend fun unselectUser() {
        userDao.unselectUser()
    }

    suspend fun getSelectedUser(): User {
        val selectedUser = userDao.getSelectedUser()
        return selectedUser ?: userDao.getDefaultUser()?.also {
            userDao.selectUser(it.username)
        } ?: throw IllegalStateException("Default user not found in the database.")
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    // Dene funksjonen er kun til testing.
    suspend fun clearAllUsers() {
        userDao.clearAllUsers()
    }

//    suspend fun addToAchievements(username: String, distance: Double) {
//        userDao.addDataToAchievements(username, distance)
//    }
}