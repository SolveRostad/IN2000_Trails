package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.repository

import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.datasource.LocationForecastDatasource
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.models.Locationforecast

/**
 * Repository for fetching weather data.
 * @param fetcher datasource for fetching weather data.
 */
class LocationForecastRepository(private val fetcher: LocationForecastDatasource) {
    /**
     * Gets the weather forecast for a specific location.
     * @param lat latitude of location.
     * @param lon longitude of location.
     * @return Locationforecast object or null if request fails.
     */
    fun getForecast(lat: Double, lon: Double): Locationforecast? {
        return fetcher.getLocationForecast(lat, lon)
    }
}
