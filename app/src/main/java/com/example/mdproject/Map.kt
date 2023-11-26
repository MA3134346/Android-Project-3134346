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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun Map(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        selectMenu()
    }
}

@Composable
fun selectMenu() {
    var selectedTour by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf("") }
    var selectedWaypoint by remember { mutableStateOf<WayPoint?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text("Select tour")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { /*TODO*/ }) {
                Text("Select group")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { /*TODO*/ }) {
                Text("Select waypoint")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text("currently selected: placeholder")
        }
        Box(modifier = Modifier.weight(1f)){
            OsmMapView()
        }
    }
}
@Composable
fun OsmMapView() {
    val context = LocalContext.current

    Configuration.getInstance().load(context, context.getSharedPreferences("osm_pref", Context.MODE_PRIVATE))
    AndroidView(factory = { ctx ->
        MapView(ctx).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)

            val mapController = this.controller
            mapController.setZoom(9.5)
            val startPoint = GeoPoint(48.8583, 2.2944)
            mapController.setCenter(startPoint)
        }
    }, update = {

    })
}

@Preview(showBackground = true)
@Composable
fun LevelPreview(){
    Map()
}