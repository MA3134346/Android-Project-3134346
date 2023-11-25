package com.example.mdproject

import android.location.Location
import androidx.compose.runtime.mutableStateListOf

data class WayPoint(var name: String, var location: Location, var group: String)
object WayPointManager {
    private val _waypoints = mutableStateListOf(
        WayPoint("baghdad", Location("baghdad").apply { latitude = 37.7749; longitude = -122.4194 }, "Group1"),
        WayPoint("timbuktu", Location("timbuktu").apply { latitude = 40.7128; longitude = -74.0060 }, "Group2"),
        WayPoint("spar", Location("spar").apply { latitude = 53.331685; longitude = -6.278487 }, "Group3")
    )

    val waypoints: List<WayPoint>
        get() = _waypoints
    fun addWaypoint(waypoint: WayPoint) {
        _waypoints.add(waypoint)
    }
    fun updateWaypoint(oldName: String, updatedWaypoint: WayPoint) {
        val index = _waypoints.indexOfFirst { it.name == oldName }
        if (index != -1) {
            _waypoints[index] = updatedWaypoint
        }
    }
}