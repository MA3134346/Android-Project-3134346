package com.example.mdproject.ui.theme

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mdproject.WayPoint
import com.example.mdproject.WayPointManager


@Composable
fun GPS() {
    //for floating action button
    var showAddDialog by remember { mutableStateOf(false) }
    //for editing a waypoint
    var editingItem by remember { mutableStateOf<WayPoint?>(null) }

    //goes through the list of waypoints and shows them on screen
    Column {
        //header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Name", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("Group", modifier = Modifier.weight(1.1f), fontWeight = FontWeight.Bold)
        }
        LazyColumn {
            items(WayPointManager.waypoints) { item ->
                //for editing and deleting
                var showMenu by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // waypoints name
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = item.name)
                    }
                    // waypoints group
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(text = item.group)
                    }
                    //button for each of the entries
                    Box {
                        IconButton(
                            onClick = { showMenu = !showMenu }
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        //to select either delete or edit
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    showMenu = false
                                    editingItem = item //set the item to be edited
                                },
                                text = { Text("Edit") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    showMenu = false
                                    showDeleteDialog = true
                                },
                                text = { Text("Delete") }
                            )
                        }
                        if (showDeleteDialog) {
                            DeleteDialog(
                                onConfirm = {
                                    WayPointManager.deleteWayPoint(item)
                                    showDeleteDialog = false
                                },
                                onDismiss = { showDeleteDialog = false }
                            )
                        }
                    }
                }
                Divider()
            }
        }
    }
    //when editingItem is set open EditDialog, when confirm is clicked overwrite waypoint
    editingItem?.let { waypoint ->
        EditDialog(
            item = waypoint,
            onDismiss = { editingItem = null },
            onConfirm = { oldName, updatedWaypoint ->
                WayPointManager.updateWaypoint(oldName, updatedWaypoint)
                editingItem = null
            }
        )
    }
    //to add new entries
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp),
            onClick = { showAddDialog = true },
            content = { Icon(Icons.Default.Add, contentDescription = "Add") }
        )
    }
    if (showAddDialog) {
        AddDialog() { showAddDialog = false }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(onDismiss: () -> Unit) {
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
                        WayPointManager.addWaypoint(WayPoint(
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
@Composable
fun DeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Waypoint") },
        text = { Text("you sure?") },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Yes") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("No") }
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    item: WayPoint,
    onDismiss: () -> Unit,
    onConfirm: (String, WayPoint) -> Unit
) {
    var newName by remember { mutableStateOf(item.name) }
    var newGroup by remember { mutableStateOf(item.group) }
    var newLong by remember { mutableDoubleStateOf(item.location.longitude) }
    var newLat by remember { mutableDoubleStateOf(item.location.latitude) }
    var oldName = remember { item.name }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Waypoint") },
        text = {
            Column {
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newGroup,
                    onValueChange = { newGroup = it },
                    label = { Text("Group") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newLong.toString(),
                    onValueChange = { newLong = it.toDouble() },
                    label = { Text("Long") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newLat.toString(),
                    onValueChange = { newLat = it.toDouble() },
                    label = { Text("Lat") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedItem = item.copy(name = newName, location = Location(newName).apply { latitude = newLat; longitude = newLong}, group = newGroup)
                    onConfirm(oldName, updatedItem)
                    onDismiss()
                }
            ) { Text("Confirm") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}


@Preview(showBackground = true)
@Composable
fun GPSPreview(){
    GPS()
}
