package no.uio.ifi.in2000_gruppe3.data.favorites.datasource

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature

val Context.favoritesDataStore: DataStore<Preferences> by preferencesDataStore(name = "feature_preferences")

class FavoritesDataSource(){
    val Context.favoritesDataStore: DataStore<Preferences> by preferencesDataStore(name = "feature_preferences")

    private val json = Json{ ignoreUnknownKeys = true }
    private val FEATURE_KEY = stringPreferencesKey("feature_data")

    suspend fun saveFeatureString(featureString: String, context: Context) {
        context.favoritesDataStore.edit { preferences ->
            preferences[FEATURE_KEY] = featureString
        }
    }

    fun getFeatureString(context: Context): Flow<String?> {
        return context.favoritesDataStore.data.map { preferences ->
            preferences[FEATURE_KEY]
        }
    }

    fun serializeFeatures(features: List<Feature>): String {
        return json.encodeToString(features)
    }

    fun deserializeFeatures(featuresString: String): List<Feature> {
        return try {
            json.decodeFromString<List<Feature>>(featuresString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}