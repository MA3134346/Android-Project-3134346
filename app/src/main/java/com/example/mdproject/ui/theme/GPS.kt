package com.example.mdproject.ui.theme

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class WayPoint(var name: String, var location: Location, var group: String)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GPS() {
    val waypoints = remember { mutableStateListOf(
        WayPoint("baghdad", Location("baghdad").apply { latitude = 37.7749; longitude = -122.4194 }, "Group1"),
        WayPoint("timbuktu", Location("timbuktu").apply { latitude = 40.7128; longitude = -74.0060 }, "Group2")
    ) }

    var showDialog by remember { mutableStateOf(false) }

    LazyColumn {
        items(waypoints) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    var newName by remember { mutableStateOf(item.name) }
                    TextField(
                        value = newName,
                        onValueChange = {
                            newName = it
                            item.name = newName
                            item.location.provider = newName
                        }
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    var newGroup by remember { mutableStateOf(item.group) }
                    TextField(
                        value = newGroup,
                        onValueChange = {
                            newGroup = it
                            item.group = newGroup
                        }
                    )
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp),
            onClick = { showDialog = true },
            content = { Icon(Icons.Default.Add, contentDescription = "Add") }
        )
    }
    if (showDialog) {
        AddDialog(waypoints) { showDialog = false }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(waypoints: SnapshotStateList<WayPoint>, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Waypoint") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = group,
                    onValueChange = { group = it },
                    label = { Text("Group") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        waypoints.add(WayPoint(
                            name,
                            Location(name).apply { latitude = 37.7749; longitude = -122.4194 },
                            group
                        ))
                        onDismiss()
                    }
                }
            )
            { Text("OK") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GPSPreview(){
    GPS()
}

