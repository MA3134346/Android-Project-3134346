package com.example.mdproject

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
/*
data class WayPoint(var name: String, var longitude: Double, var latitude: Double, var group: String)

class WaypointDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "waypoints")
        val WAYPOINT_NAME = stringPreferencesKey("name")
        val WAYPOINT_LONG = doublePreferencesKey("long")
        val WAYPOINT_LAT = doublePreferencesKey("lat")
        val WAYPOINT_GROUP = stringPreferencesKey("group")
    }

    suspend fun saveWaypoint(waypoint: WayPoint) {
        context.dataStore.edit { preferences ->
            preferences[WAYPOINT_NAME] = waypoint.name
            preferences[WAYPOINT_LONG] = waypoint.longitude
            preferences[WAYPOINT_LAT] = waypoint.latitude
            preferences[WAYPOINT_GROUP] = waypoint.group
        }
    }

    fun getWaypoint(): Flow<WayPoint> {
        return context.dataStore.data.map { preferences ->
            WayPoint(
                name = preferences[WAYPOINT_NAME] ?: "",
                longitude = preferences[WAYPOINT_LONG]?: 0.0,
                latitude = preferences[WAYPOINT_LAT]?: 0.0,
                group = preferences[WAYPOINT_GROUP] ?: ""
            )
        }
    }
    fun sampleData(): Flow<WayPoint> = flow {
        // Simulate a flow of data for demonstration purposes
        emit(WayPoint("Sample Waypoint", 0.0, 0.0, "Sample Group"))
    }
}

*/