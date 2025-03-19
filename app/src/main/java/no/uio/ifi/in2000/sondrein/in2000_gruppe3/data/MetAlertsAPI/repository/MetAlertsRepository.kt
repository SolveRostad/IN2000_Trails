package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI.repository

import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI.datasource.MetAlertsDatasource
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI.models.MetAlerts

/**
 * Repository for fetching MetAlerts data
 * @param fetcher datasource for fetching MetAlerts data
 */
class MetAlertsRepository() {
    private val fetcher = MetAlertsDatasource()

    /**
     * Gets the MetAlerts data
     * @return MetAlerts object or null if request fails
     */
    fun getAlerts(): MetAlerts? {
        return fetcher.getMetAlerts()
    }
}
