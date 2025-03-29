package no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Geometry
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.PropertiesX

class HikeScreenViewModel : ViewModel() {
    private val _hikeScreenUIState = MutableStateFlow<HikeScreenUIState>(
        HikeScreenUIState(
            feature = Feature(
                Geometry(listOf(), "error"),
                properties = PropertiesX(
                    cmt = "error",
                    desc = "error",
                    distance_meters = -1.0,
                    distance_to_point = -1.0,
                    fid = -1,
                    name = "error",
                    src = "error",
                    type = "error",
                    gradering = "error"
                ),
                "error"
            )
        )
    )
    val hikeScreenUIState: StateFlow<HikeScreenUIState> = _hikeScreenUIState.asStateFlow()

    fun updateHike(feature: Feature) {
        _hikeScreenUIState.update {
            it.copy(feature = feature)
        }
    }

    fun updateDate(day: String, date: String, formattedDate: String) {
        _hikeScreenUIState.update {
            it.copy(
                day = day,
                date = date,
                formattedDate = formattedDate
            )
        }
    }
}

data class HikeScreenUIState(
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val feature: Feature,
    val day: String = "",
    val date: String = "",
    val formattedDate: String = ""
)