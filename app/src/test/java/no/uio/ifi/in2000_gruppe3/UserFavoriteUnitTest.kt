package no.uio.ifi.in2000_gruppe3

import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.uio.ifi.in2000_gruppe3.data.database.User
import no.uio.ifi.in2000_gruppe3.data.database.UserFavoritesDatabase
import no.uio.ifi.in2000_gruppe3.data.favorites.FavoriteRepository
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository.HikeAPIRepository
import no.uio.ifi.in2000_gruppe3.data.user.UserRepository
import no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen.OpenAIViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesScreenViewModel
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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


    //Dummy data til testene
    val bruker1 = User(username = "Aanund")
    val bruker2 = User(username = "Victor")

    @After
    fun tearDown() {
        userDatabase.close()
    }

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

            assertEquals(selectedUser.username, bruker1.username, "Bruker ble ikke valgt")
        }

        println("---testSelectUser PASSERT---")
    }

    //Tester for favorites view model.
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testAddFavorite() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            userRepository.clearAllUsers()
            userRepository.addUser(bruker1)
            userRepository.selectUser(bruker1.username)

            val favoritesScreenViewModel = FavoritesScreenViewModel(
                application = application,
                favoritesRepository = FavoriteRepository(favoriteDao),
                userRepository = userRepository,
                hikeAPIRepository = HikeAPIRepository(OpenAIViewModel())
            )

            favoritesScreenViewModel.addFavorite(1)

            var favorites = favoritesScreenViewModel.getAllFavorites(bruker1.username)
            // Dette var den eneste måten jeg klarte å vente på at viewmodelscope
            // coroutine launchen ble ferdig på før hovedtråen fortsetter...
            while (favorites.isEmpty()) {
                favorites = favoritesScreenViewModel.getAllFavorites(bruker1.username)
            }

            assertEquals(1, favorites.last(), "Favoritt ble ikke lagt til")
        } finally {
            Dispatchers.resetMain()
        }
        println("---testAddFavorite PASSERT---")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDeleteFavorite() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            userRepository.clearAllUsers()
            userRepository.addUser(bruker1)
            userRepository.selectUser(bruker1.username)

            val favoritesScreenViewModel = FavoritesScreenViewModel(
                application = application,
                favoritesRepository = FavoriteRepository(favoriteDao),
                userRepository = userRepository,
                hikeAPIRepository = HikeAPIRepository(OpenAIViewModel())
            )
            favoritesScreenViewModel.addFavorite(1)
            var favorites = favoritesScreenViewModel.getAllFavorites(bruker1.username)
            while (favorites.isEmpty()) {
                favorites = favoritesScreenViewModel.getAllFavorites(bruker1.username)
            }
            favoritesScreenViewModel.deleteFavorite(1)
            while (favorites.isNotEmpty()) {
                favorites = favoritesScreenViewModel.getAllFavorites(bruker1.username)
            }

            assertTrue(favorites.isEmpty(), "Favoritt ble ikke slettet")

            println("---testDeleteFavorite PASSERT---")
        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetAllFavorites() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            userRepository.clearAllUsers()
            userRepository.addUser(bruker1)
            userRepository.selectUser(bruker1.username)

            val favoritesScreenViewModel = FavoritesScreenViewModel(
                application = application,
                favoritesRepository = FavoriteRepository(favoriteDao),
                userRepository = userRepository,
                hikeAPIRepository = HikeAPIRepository(OpenAIViewModel())
            )

            favoritesScreenViewModel.addFavorite(1)
            favoritesScreenViewModel.addFavorite(2)

            var favorites = favoritesScreenViewModel.getAllFavorites(bruker1.username)

            while (favorites.size < 2) {
                favorites = favoritesScreenViewModel.getAllFavorites(bruker1.username)
            }

            assertContains(favorites, 1, "fikk ikke hentet alle ider")
            assertContains(favorites, 2, "fikk ikke hentet alle ider")

        } finally {
            Dispatchers.resetMain()
        }
        println("---testGetAllFavorites PASSERT---")
    }
}