package com.example.mdproject.ui.theme

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mdproject.R
import com.example.mdproject.WayPoint
import com.example.mdproject.WayPointManager
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Preview(showBackground = true)
@Composable
fun Compass() {
    var useTrueNorth by remember { mutableStateOf(true) }
    var targetObj by remember { mutableStateOf(WayPoint("default", Location("default").apply { latitude = 37.7749; longitude = -122.4194 }, "default"))}
    val rot = rotationToObject(useTrueNorth, targetObj)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CompassTarget(if (useTrueNorth) "North" else targetObj.name, onTargetObjSelected = { selectedObj ->
                targetObj = selectedObj
                useTrueNorth = false
            })
            Switch(
                checked = useTrueNorth,
                onCheckedChange = { useTrueNorth = it },
                modifier = Modifier.padding(8.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DegreeText(rot)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalArrangement = Arrangement.Center
        ) {
            CompassSprite(rot)
        }
    }
}

fun rotationToTarget(): Float {

    return 0.0f
}

@Composable
fun CompassTarget(target: String, onTargetObjSelected: (WayPoint) -> Unit){
    var expanded by remember { mutableStateOf(false) }
    Box {
        Column {
            Text(text = "Target: $target", modifier = Modifier.clickable { expanded = true })
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            WayPointManager.waypoints.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name)},
                    onClick = {onTargetObjSelected(item)}
                )
        }
        }
    }
}
@Composable
fun DegreeText(rotation: Float) {
    Text(text = "${rotation.toInt()}°")
}

@Composable
fun CompassSprite(rotation: Float) {
    Image(
        painter = painterResource(R.drawable.compass),
        contentDescription = "Compass",
        modifier = Modifier.graphicsLayer {
            rotationZ = rotation
        }
    )
}

@Composable
fun rotationToObject(useTrueNorth: Boolean, targetObj: WayPoint?): Float {
    var rot by remember { mutableFloatStateOf(0f) }

    val sensorManager: SensorManager = LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val currentLocation by remember { mutableStateOf(Location("test").apply { latitude = 53.330309; longitude = -6.278394 }) }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

            override fun onSensorChanged(event: SensorEvent) {
                val rotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                val orientationValues = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientationValues)

                val azimuth = Math.toDegrees(orientationValues[0].toDouble()).toFloat()
                val adjustedAzimuth = if (azimuth < 0) azimuth + 360 else azimuth

                rot = if (useTrueNorth) {
                    adjustedAzimuth
                } else {
                    targetObj?.location?.let { targetLocation ->
                        (adjustedAzimuth - bearingToLocation(currentLocation, targetLocation) + 360) % 360
                    } ?: adjustedAzimuth //if targetObj is null default to north
                }
            }
        }
        sensorManager.registerListener(
            listener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
    return rot
}

fun bearingToLocation(currentLocation: Location, targetLocation: Location): Float {
    val lat1 = Math.toRadians(currentLocation.latitude)
    val lon1 = Math.toRadians(currentLocation.longitude)
    val lat2 = Math.toRadians(targetLocation.latitude)
    val lon2 = Math.toRadians(targetLocation.longitude)

    val x = sin(lon2 - lon1) * cos(lat2)
    val y = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lon2 - lon1)

    val initialBearing = Math.toDegrees(atan2(x, y)).toFloat()

    return (initialBearing + 360) % 360
}
