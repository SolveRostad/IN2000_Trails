package no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Geometry
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Properties

class HikeScreenViewModel: ViewModel() {
    private val _hikeScreenUIState = MutableStateFlow<HikeScreenUIState> (
        HikeScreenUIState(
            feature = Feature(
                Geometry(listOf(), "error"), Properties(0, listOf(), "error"), "error")
        )
    )
    val hikeScreenUIState: StateFlow<HikeScreenUIState> = _hikeScreenUIState.asStateFlow()

    fun updateHike(feature: Feature) {
        _hikeScreenUIState.update {
            it.copy(feature = feature)
        }
    }
}

data class HikeScreenUIState(
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val feature: Feature
)