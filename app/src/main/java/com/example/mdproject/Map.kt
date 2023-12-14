package com.example.mdproject

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun Map(){
    //currently selected waypoint
    var selectedWaypoint by remember { mutableStateOf<WayPoint?>(null) }
    Column(modifier = Modifier.fillMaxSize()) {
        //select menu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            //display select menu
            selectMenu(selectedWaypoint = selectedWaypoint) { waypoint ->
                selectedWaypoint = waypoint //new waypoint is selected
            }
        }
        //map
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1.5f)) {
            //display map with selected waypoint
            OsmMapView(selectedWaypoint)
        }
    }
}

@Composable
fun selectMenu(selectedWaypoint: WayPoint?, onWaypointSelected: (WayPoint) -> Unit ) {
    //dropdown states
    var selectedGroup by remember { mutableStateOf("") }
    var expandedGroup by remember { mutableStateOf(false) }
    var expandedWaypoint by remember { mutableStateOf(false) }

    //data sources
    val groups = WayPointManager.groups
    val waypoints = WayPointManager.waypoints

    //select menu
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //group / waypoint select
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            //group select button / dropdown
            Box {
                Button(onClick = { expandedGroup = true }) {
                    Text("Select group")
                }
                DropdownMenu(
                    expanded = expandedGroup,
                    onDismissRequest = { expandedGroup = false }
                ) {
                    groups.forEach { group ->
                        DropdownMenuItem(onClick = {
                            selectedGroup = group
                            expandedGroup = false
                        }, text = { Text(group) })
                    }
                }
            }

            //waypoint select button / dropdown
            Box {
                Button(onClick = { expandedWaypoint = true }) {
                    Text("Select waypoint")
                }
                DropdownMenu(
                    expanded = expandedWaypoint,
                    onDismissRequest = { expandedWaypoint = false }
                ) {
                    waypoints.forEach { waypoint  ->
                        DropdownMenuItem(onClick = {
                            onWaypointSelected(waypoint)
                            expandedGroup = false
                        }, text = { Text(waypoint.name) })
                    }
                }
            }

            //selected group and waypoint
            Text("Currently selected group: $selectedGroup")
            selectedWaypoint?.let {
                Text("Currently selected waypoint: ${it.name}")
            }
        }
    }
}
@Composable
fun OsmMapView(selectedWaypoint: WayPoint?) {
    //context for osm configuration
    val context = LocalContext.current

    //load osm preferences
    Configuration.getInstance().load(context, context.getSharedPreferences("osm_pref", Context.MODE_PRIVATE))

    val currentLocationFlow = observeLocation()
    val currentLocation by currentLocationFlow.collectAsState()

    //create and configure map view
    AndroidView(factory = { ctx ->
        MapView(ctx).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.5)
            controller.setCenter(currentLocation?.let { GeoPoint(it.latitude, it.longitude) })
            println(currentLocation.toString())

        }
    }, update = { mapView ->
        mapView.overlays.clear() //clear other waypoints

        //add a marker to the map
        selectedWaypoint?.let { waypoint ->
            val mapController = mapView.controller
            val waypointGeoPoint = GeoPoint(waypoint.location.latitude, waypoint.location.longitude)

            mapController.setCenter(waypointGeoPoint)//center to waypoint

            //add marker to overlay
            val marker = Marker(mapView)
            marker.position = waypointGeoPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            marker.title = waypoint.name

            mapView.overlays.add(marker)
            mapView.invalidate() //refresh
        }
    })
}

@Preview(showBackground = true)
@Composable
fun LevelPreview(){
    Map()
}