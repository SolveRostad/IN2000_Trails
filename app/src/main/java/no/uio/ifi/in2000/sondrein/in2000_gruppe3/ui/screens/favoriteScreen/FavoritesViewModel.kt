package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.favoriteScreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.Favorites.repository.FeatureRepository
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Feature

class FavoritesViewModel: ViewModel() {
    private val favoriteRepository =  FeatureRepository()

    private val _favortiesScreenUIState = MutableStateFlow<FavoritesScreenUIState> (
        FavoritesScreenUIState(favorites = emptyList())
    )
    val favoritesScreenUIState: StateFlow<FavoritesScreenUIState> = _favortiesScreenUIState.asStateFlow()

    val hikes = mutableStateListOf<Feature>()

    fun addHike (feature: Feature, context: Context) {
        viewModelScope.launch{
            _favortiesScreenUIState.update{
                it.copy(isLoading = true)
            }
            try {_favortiesScreenUIState.update {
                val updatedHikes = hikes + feature
                it.copy(favorites = updatedHikes)            }
                saveHikes(hikes, context)
                Log.i("La til tur i favorites", "${hikes}")
            }
            catch (e: Exception){
                _favortiesScreenUIState.update{
                    it.copy(isError = true, errorMessage = e.message ?: "Unknown error")
                }
            }
            finally {
                _favortiesScreenUIState.update{
                    it.copy(isLoading = false)
                }
            }

        }
    }

    fun saveHikes(features: List<Feature>, context: Context){
        viewModelScope.launch {
            _favortiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                favoriteRepository.saveHikes(features, context)
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

    fun deleteHike(feature: Feature, context: Context){
        viewModelScope.launch {
            _favortiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val updatedHikes = favoriteRepository.deleteHike(feature, context)
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
}

data class FavoritesScreenUIState(
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val favorites: List<Feature>?,
)