package no.uio.ifi.in2000_gruppe3

import androidx.room.Room
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000_gruppe3.data.database.User
import no.uio.ifi.in2000_gruppe3.data.database.UserFavoritesDatabase
import no.uio.ifi.in2000_gruppe3.data.favorites.FavoriteRepository
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository.HikeAPIRepository
import no.uio.ifi.in2000_gruppe3.data.user.UserRepository
import no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen.OpenAIViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesScreenViewModel
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Testklasse for UserRepository og favoritesViewModel.
 * Tester noen utvalgte funksjoner fra userDao, userRepository og favoritesViewModel
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
    private val favoriteDao = userDatabase.favoriteDao()

    private val userRepository = UserRepository(userDao)

    val favoritesScreenViewModel = FavoritesScreenViewModel(
        application = application,
        favoritesRepository = FavoriteRepository(favoriteDao),
        userRepository = userRepository,
        hikeAPIRepository = HikeAPIRepository(OpenAIViewModel())
    )

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

    //Tester for favorites view model.
    @Test
    fun testAddFavorite() {
        favoritesScreenViewModel.addFavorite(1)
        val favorites = favoritesScreenViewModel.getAllFavorites()
        assertEquals(favorites.last(), 1, "Favoritt ble ikke lagt til")

        println("---testAddFavorite PASSERT---")
    }

    @Test
    fun testDeleteFavorite() {
        favoritesScreenViewModel.addFavorite(1)
        favoritesScreenViewModel.deleteFavorite(1)

        val favorites = favoritesScreenViewModel.getAllFavorites()

        assertFalse(favorites.isEmpty(), "Favoritt ble ikke slettet")

        println("---testDeleteFavorite PASSERT---")
    }

    @Test
    fun testGetAllFavorites() {
        favoritesScreenViewModel.addFavorite(1)
        favoritesScreenViewModel.addFavorite(2)

        val favorites: List<Int> = favoritesScreenViewModel.getAllFavorites()

        assertContains(favorites, 1, "hike id 1 ble ikke hentet")
        assertContains(favorites, 2, "hike id 2 ble ikke hentet")

        println("---testGetAllFavorites PASSERT---")
    }
}