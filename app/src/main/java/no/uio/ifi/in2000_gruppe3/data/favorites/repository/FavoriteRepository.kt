package no.uio.ifi.in2000_gruppe3.data.favorites

import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000_gruppe3.data.database.Favorite
import no.uio.ifi.in2000_gruppe3.data.database.FavoriteDao
import no.uio.ifi.in2000_gruppe3.data.database.UserDao

class FavoriteRepository(private val favoriteDao: FavoriteDao) {
    suspend fun addFavorite(newFavorite: Favorite) {
        favoriteDao.saveFavorite(newFavorite)
    }

    suspend fun deleteFavorite(favoriteToRemove: Favorite) {
        favoriteDao.deleteFavorite(favoriteToRemove)
    }

    suspend fun getAllFavorites(username: String): List<Favorite> {
        return favoriteDao.getAllFavorites(username)
    }
}