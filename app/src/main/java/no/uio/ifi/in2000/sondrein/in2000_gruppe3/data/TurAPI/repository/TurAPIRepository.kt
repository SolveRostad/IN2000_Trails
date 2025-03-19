package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.repository

import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.datasource.TurAPIDatasource
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Turer

class TurAPIRepository {
    private val turAPIDatasource = TurAPIDatasource()

    suspend fun getTurer(lat: Double, lng: Double, limit: Int): Turer {
        return turAPIDatasource.getTurer(lat, lng, limit)
    }
}
