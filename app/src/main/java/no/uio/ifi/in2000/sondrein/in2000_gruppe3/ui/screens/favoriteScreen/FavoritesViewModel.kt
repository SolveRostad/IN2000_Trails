package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.favoriteScreen

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.Favorites.repository.FeatureRepository
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Feature

class FavoritesViewModel(application: Application): AndroidViewModel(application) {
    private val favoriteRepository =  FeatureRepository()
    private val appContext = getApplication<Application>().applicationContext

    private val _favortiesScreenUIState = MutableStateFlow<FavoritesScreenUIState> (
        FavoritesScreenUIState(favorites = emptyList())
    )
    val favoritesScreenUIState: StateFlow<FavoritesScreenUIState> = _favortiesScreenUIState.asStateFlow()

    val hikes = mutableStateListOf<Feature>()

    init{
        loadSavedHikes()
    }

    private fun loadSavedHikes() {
        viewModelScope.launch {
            try {
                favoriteRepository.getHikes(appContext).collect { loadedHikes ->
                    hikes.clear()
                    hikes.addAll(loadedHikes)
                    _favortiesScreenUIState.update {
                        it.copy(favorites = loadedHikes)
                    }
                }
            } catch (e: Exception) {
                _favortiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message ?: "Unknown error")
                }
            }
        }
    }

    fun addHike(feature: Feature) {
        viewModelScope.launch {
            _favortiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                if (!hikes.contains(feature)) {
                    hikes.add(feature)
                    _favortiesScreenUIState.update {
                        it.copy(favorites = hikes.toList())
                    }
                    viewModelSaveHikes(hikes)
                    Log.d("La til tur i favorites", "$hikes")
                }
            } catch (e: Exception) {
                _favortiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message ?: "Unknown error")
                }
            } finally {
                _favortiesScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun viewModelSaveHikes(features: List<Feature>){
        viewModelScope.launch {
            _favortiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                favoriteRepository.saveHikes(features, appContext)
                _favortiesScreenUIState.update {
                    it.copy(favorites = features, isError = false)
                }
            } catch (e: Exception) {
                _favortiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message ?: "Unknown error")
                }
            } finally {
                _favortiesScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }


    }

    fun deleteHike(feature: Feature) {
        viewModelScope.launch {
            _favortiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val updatedHikes = favoriteRepository.deleteHike(feature, appContext)
                hikes.clear()
                hikes.addAll(updatedHikes)
                _favortiesScreenUIState.update {
                    it.copy(favorites = updatedHikes, isError = false)
                }
            } catch (e: Exception) {
                _favortiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message ?: "Unknown error")
                }
            } finally {
                _favortiesScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun isHikeFavorite(feature: Feature): Boolean {
        return hikes.any { it.properties.rutenavn == feature.properties.rutenavn }
    }
}

data class FavoritesScreenUIState(
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val favorites: List<Feature>,
)