package com.example.mdproject

import android.location.Location
import androidx.compose.runtime.mutableStateListOf

data class WayPoint(var name: String, var location: Location, var group: String)
object WayPointManager {
    //list of waypoints for test purposes TODO: make it save/recover when app closes/opens
    private val _waypoints = mutableStateListOf(
        WayPoint("baghdad", Location("baghdad").apply { latitude = 37.7749; longitude = -122.4194 }, "Group1"),
        WayPoint("timbuktu", Location("timbuktu").apply { latitude = 40.7128; longitude = -74.0060 }, "Group2"),
        WayPoint("spar", Location("spar").apply { latitude = 53.331685; longitude = -6.278487 }, "Group3")
    )

    //retrieve waypoints
    val waypoints: List<WayPoint>
        get() = _waypoints

    //get all unique groups
    val groups: List<String>
        get() = waypoints.map { it.group }.toSet().toList()

    //add waypoint
    fun addWaypoint(waypoint: WayPoint) {
        _waypoints.add(waypoint)
    }
    //update a waypoint
    fun updateWaypoint(oldName: String, updatedWaypoint: WayPoint) {
        val index = _waypoints.indexOfFirst { it.name == oldName }
        if (index != -1) {
            _waypoints[index] = updatedWaypoint
        }
    }
    //delete a waypoint
    fun deleteWayPoint(waypoint: WayPoint) {
        _waypoints.remove(waypoint)
    }

}