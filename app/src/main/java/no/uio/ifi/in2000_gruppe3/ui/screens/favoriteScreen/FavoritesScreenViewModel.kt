package no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.database.Favorite
import no.uio.ifi.in2000_gruppe3.data.favorites.repository.FavoriteRepository
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository.HikeAPIRepository
import no.uio.ifi.in2000_gruppe3.data.profile.repository.ProfileRepository
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel

class FavoritesScreenViewModel(
    application: Application,
    private val favoriteRepository: FavoriteRepository,
    private val profileRepository: ProfileRepository,
    private val hikeAPIRepository: HikeAPIRepository,
    private val mapboxViewModel: MapboxViewModel?, // must be nullable for testing
) : AndroidViewModel(application) {

    private val _favoriteScreenUIState = MutableStateFlow(
        FavoriteScreenUIState(
            favorites = emptyList(),
            userLocation = Point.fromLngLat(10.441649, 59.542819),
            isLoading = true
        )
    )
    val favoriteScreenUIState: StateFlow<FavoriteScreenUIState> =
        _favoriteScreenUIState.asStateFlow()

    init {
        loadFavoritesSequentially()
    }

    private fun loadFavoritesSequentially() {
        viewModelScope.launch {
            try {
                _favoriteScreenUIState.update {
                    it.copy(isLoading = true)
                }

                // Get the selected profile
                val profile = profileRepository.getSelectedProfile()
                _favoriteScreenUIState.update {
                    it.copy(username = profile.username)
                }

                // Get all favorites
                val favorites = getAllFavorites(profile.username)
                _favoriteScreenUIState.update {
                    it.copy(favorites = favorites)
                }

                // Convert favorites to features
                if (favorites.isNotEmpty()) {
                    val favoriteFeatures = hikeAPIRepository.getHikesById(
                        favorites,
                        _favoriteScreenUIState.value.userLocation
                    )
                    _favoriteScreenUIState.update {
                        it.copy(convertedFavorites = favoriteFeatures, isLoading = false)
                    }
                } else {
                    _favoriteScreenUIState.update {
                        it.copy(isLoading = false)
                    }
                }
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "loadFavoritesSequentially: ${e.message}")
                _favoriteScreenUIState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = e.message.toString()
                    )
                }
            }
        }
    }

    fun loadFavorites() {
        loadFavoritesSequentially()
    }

    fun setProfile() {
        viewModelScope.launch {
            _favoriteScreenUIState.update {
                it.copy(username = profileRepository.getSelectedProfile().username)
            }
        }
    }

    fun updateUserLocationFromMapbox() {
        val latestPosition = mapboxViewModel?.mapboxUIState?.value?.latestUserPosition
        if (latestPosition != null) {
            updateUserLocation(latestPosition)
        }
    }

    fun updateUserLocation(point: Point) {
        _favoriteScreenUIState.update {
            it.copy(userLocation = point)
        }
    }

    // For testing
    suspend fun getAllFavorites(username: String): List<Int> {
        return favoriteRepository.getAllFavorites(username)
    }

    fun isHikeFavorite(feature: Feature): Boolean {
        return _favoriteScreenUIState.value.favorites.contains(feature.properties.fid)
    }

    fun addFavorite(id: Int) {
        viewModelScope.launch {
            _favoriteScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val currentProfile = profileRepository.getSelectedProfile()
                val newFavorite = Favorite(currentProfile.username, id)
                favoriteRepository.addFavorite(newFavorite)

                val updatedFavorites = _favoriteScreenUIState.value.favorites + newFavorite.hikeId
                _favoriteScreenUIState.update {
                    it.copy(favorites = updatedFavorites)
                }

                val favoriteFeatures = hikeAPIRepository.getHikesById(
                    updatedFavorites,
                    _favoriteScreenUIState.value.userLocation
                )
                _favoriteScreenUIState.update {
                    it.copy(convertedFavorites = favoriteFeatures)
                }
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "addFavorites: ${e.message}")
                _favoriteScreenUIState.update {
                    it.copy(
                        isError = true,
                        errorMessage = e.message.toString()
                    )
                }
            } finally {
                _favoriteScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun deleteFavorite(id: Int) {
        viewModelScope.launch {
            _favoriteScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val currentProfile = profileRepository.getSelectedProfile()
                val favoriteToRemove = Favorite(currentProfile.username, id)
                favoriteRepository.deleteFavorite(favoriteToRemove)

                val updatedFavorites = _favoriteScreenUIState.value.favorites - favoriteToRemove.hikeId
                _favoriteScreenUIState.update {
                    it.copy(favorites = updatedFavorites)
                }

                val favoriteFeatures = hikeAPIRepository.getHikesById(
                    updatedFavorites,
                    _favoriteScreenUIState.value.userLocation
                )
                _favoriteScreenUIState.update {
                    it.copy(convertedFavorites = favoriteFeatures)
                }
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "deleteFavorites: ${e.message}")
                _favoriteScreenUIState.update {
                    it.copy(
                        isError = true,
                        errorMessage = e.message.toString()
                    )
                }
            } finally {
                _favoriteScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }
}

data class FavoriteScreenUIState(
    val convertedFavorites: List<Feature> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val favorites: List<Int> = emptyList(),
    val username: String = "",
    val userLocation: Point = Point.fromLngLat(10.441649, 59.542819) // Default location (Oslo, Norway)
)
