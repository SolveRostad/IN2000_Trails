package no.uio.ifi.in2000_gruppe3.data.userLocation.repository

import android.content.Context
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.StateFlow
import no.uio.ifi.in2000_gruppe3.data.userLocation.datasource.UserLocationDatasource

class UserLocationRepository(context: Context) {
    private val datasource = UserLocationDatasource(context)

    fun startLocationUpdates() {
        datasource.startLocationUpdates()
    }

    fun observeUserLocation(): StateFlow<Point?> {
        return datasource.locationFlow
    }

    fun getDefaultLocation(): Point {
        return Point.fromLngLat(59.9139, 10.7522)
    }
}