package com.example.mdproject

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
class LocationManager(context: Context) : ViewModel() {
    private val _currentLocation = MutableStateFlow<Location?>(null)
    //expose current location state
    val currentLocation: StateFlow<Location?> = _currentLocation

    //initializer to get current location when instance is created
    init {
        getCurrentLocation(context) { location ->
            _currentLocation.value = location
        }
    }

    //get current location
    private fun getCurrentLocation(context: Context, callback: (Location?) -> Unit) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //permissions
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback(null) //no permissions
            return
        }

        //location event listener
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                callback(location)
                locationManager.removeUpdates(this)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        //where location comes from (gps)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
    }
}

//observe location changes
@Composable
fun observeLocation(): StateFlow<Location?> {
    val context = LocalContext.current
    val locationViewModel = remember { LocationManager(context) }
    return locationViewModel.currentLocation
}