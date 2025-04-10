package no.uio.ifi.in2000_gruppe3

import org.junit.Test


class UserFavoriteUnitTest {
    val userRepository = TODO()
    val userViewModel = TODO()

    //Dummy data til testene
    val bruker1 = User(username = "Aanund")
    val bruker2 = User(username = "Victor")

    @Test
    fun testAddUser() {
        println("Tester å legge til bruker: $bruker1")

        userRepository.addUser(bruker1)
        assertContains(userRepository.getAllUsers(), bruker1, "Bruker ble ikke lagt til")

        println("---testAddUser PASSERT---")
    }

    @Test
    fun testDeleteUser() {
        println("Tester å slette bruker: $bruker1")

        userRepository.addUser(bruker1)
        userRepository.deleteUser(bruker1)
        assertFalse(userRepository.getAllUsers().contains(bruker1), "Bruker ble ikke slettet")

        println("---testDeleteUser PASSERT---")
    }

    @Test
    fun testGetAllUsers() {
        println("tester å hente alle brukere")

        userRepository.addUser(bruker1)
        userRepository.addUser(bruker2)

        val allUsers = userRepository.getAllUsers()

        assertContains(allUsers, bruker1, "Bruker 1 ble ikke lagt til")
        assertContains(allUsers, bruker2, "Bruker 2 ble ikke lagt til")

        println("---testGetAllUsers PASSERT---")
    }

    @Test
    fun testSelectUser() {
        println("Tester å velge bruker: $bruker1")

        userRepository.addUser(bruker1)
        userViewModel.selectUser(bruker1.username)

        val selectedUser = userViewModel.getSelectedUser()
        assertEquals(selectedUser, bruker1, "Bruker ble ikke valgt")

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