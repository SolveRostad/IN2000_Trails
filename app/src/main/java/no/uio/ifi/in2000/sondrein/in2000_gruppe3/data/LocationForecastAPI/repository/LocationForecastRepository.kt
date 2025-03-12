package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.repository

import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.datasource.FetchLocationForecast
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.models.Locationforecast

/**
 * Repository for weather data.
 * Fetches data from the API.
 */
class LocationForecastRepository(private val fetcher: FetchLocationForecast) {
    /**
     * Gets the weather forecast for a specific location.
     * @param lat Latitude of the location.
     * @param lon Longitude of the location.
     * @return Locationforecast object or null if request fails.
     */
    fun getForecast(lat: Double, lon: Double): Locationforecast? {
        return fetcher.getLocationForecast(lat, lon)
    }
}
