package no.uio.ifi.in2000_gruppe3.data.favorites.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import no.uio.ifi.in2000_gruppe3.data.favorites.datasource.FavoritesDataSource
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature

class FeatureRepository() {

    private val dataSource = FavoritesDataSource()

    suspend fun deleteHike(featureToDelete: Feature, context: Context): List<Feature> {
        val currentFeatures = getHikes(context).first()
        val updatedFeatures = currentFeatures.filter { feature ->
            feature.properties.rutenavn != featureToDelete.properties.rutenavn
        }
        saveHikes(updatedFeatures, context)
        return updatedFeatures
    }

    suspend fun saveHikes(features: List<Feature>, context: Context) {
        val featuresString = dataSource.serializeFeatures(features)
        dataSource.saveFeatureString(featuresString, context)
    }


    fun getHikes(context: Context): Flow<List<Feature>> {
        return dataSource.getFeatureString(context).map { featuresString ->
            featuresString?.let { dataSource.deserializeFeatures(it) } ?: emptyList()
        }
    }
}