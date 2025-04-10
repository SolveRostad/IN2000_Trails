package no.uio.ifi.in2000_gruppe3

import androidx.room.Room
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000_gruppe3.data.database.User
import no.uio.ifi.in2000_gruppe3.data.database.UserFavoritesDatabase
import no.uio.ifi.in2000_gruppe3.data.user.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Testklasse for UserRepository.
 * Tester noen utvalgte funksjoner fra userDao og userRepository
 * for å sjekke at SQL spørringene fungerer som de skal.
 * NB! Vi har ikke skrevet tester for ALL funksjonaliteten
 */

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class UserFavoriteUnitTest {
    private val application = RuntimeEnvironment.getApplication()
    private val userDatabase = Room.inMemoryDatabaseBuilder(
        application,
        UserFavoritesDatabase::class.java
    )
        .allowMainThreadQueries()
        .build()

    private val userDao = userDatabase.userDao()

    private val userRepository = UserRepository(userDao)
    //val userViewModel = TODO()

    //Dummy data til testene
    val bruker1 = User(username = "Aanund")
    val bruker2 = User(username = "Victor")

    @Test
    fun testAddUser() {
        println("Tester å legge til bruker: $bruker1")

        runBlocking {
            userRepository.clearAllUsers()
            userRepository.addUser(bruker1)
            assertContains(userRepository.getAllUsers(), bruker1, "Bruker ble ikke lagt til")
        }

        println("---testAddUser PASSERT---")
    }

    @Test
    fun testDeleteUser() {
        println("Tester å slette bruker: $bruker1")

        runBlocking {
            userRepository.clearAllUsers()
            userRepository.addUser(bruker1)
            userRepository.deleteUser(bruker1)
            assertFalse(userRepository.getAllUsers().contains(bruker1), "Bruker ble ikke slettet")
        }

        println("---testDeleteUser PASSERT---")
    }

    @Test
    fun testGetAllUsers() {
        println("tester å hente alle brukere")
        val allUsers: List<User>;

        runBlocking {
            userRepository.clearAllUsers()
            userRepository.addUser(bruker1)
            userRepository.addUser(bruker2)

            allUsers = userRepository.getAllUsers()

            assertContains(allUsers, bruker1, "Bruker 1 ble ikke lagt til")
            assertContains(allUsers, bruker2, "Bruker 2 ble ikke lagt til")
        }

        println("---testGetAllUsers PASSERT---")
    }

    @Test
    fun testSelectUser() {
        println("Tester å velge bruker: $bruker1")
        val selectedUser: User?

        runBlocking {
            userRepository.clearAllUsers()
            userRepository.addUser(bruker1)
            userRepository.selectUser(bruker1.username)
            selectedUser = userRepository.getSelectedUser()

            assertEquals(selectedUser?.username, bruker1.username, "Bruker ble ikke valgt")
        }

        println("---testSelectUser PASSERT---")
    }

    @Test
    fun testAddFavorite() {
        //TODO test for å legge til hikeID i favoritter
    }

    @Test
    fun testDeleteFavorite() {
        //TODO test for å fjerne hikeID fra favoritter
    }

    @Test
    fun testGetAllFavorites() {
        //TODO test for å hente alle favoritter
    }
}