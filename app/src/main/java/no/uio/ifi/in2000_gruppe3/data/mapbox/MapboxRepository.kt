package no.uio.ifi.in2000_gruppe3.data.mapbox

import android.content.Context
import com.mapbox.maps.MapView

class MapboxRepository {
    private lateinit var mapView: MapView

    fun createMap(context: Context): MapView {
        mapView = MapView(context)
        return mapView
    }
}