package com.example.mdproject

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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


@Composable
fun GPS() {
    //states for dialog
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<WayPoint?>(null) }

    //display waypoints list
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
        //list of waypoints
        LazyColumn {
            items(WayPointManager.waypoints) { item ->
                //states for dialog
                var showMenu by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }

                //waypoint row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Waypoint name and group
                    Column(modifier = Modifier.weight(1f)) { Text(text = item.name) }
                    Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) { Text(text = item.group) }

                    //button for each of the entries
                    Box {
                        IconButton(onClick = { showMenu = !showMenu }) {
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
    //edit dialog
    editingItem?.let { waypoint ->
        EditDialog(
            item = waypoint,
            onDismiss = { editingItem = null },
            onConfirm = { oldName, updatedWaypoint ->
                //when confirm in the edit dialog is clicked update waypoint
                WayPointManager.updateWaypoint(oldName, updatedWaypoint)
                editingItem = null
            }
        )
    }
    //fab to add new waypoints
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            modifier = Modifier.padding(16.dp).size(56.dp),
            onClick = { showAddDialog = true },
            content = { Icon(Icons.Default.Add, contentDescription = "Add") }
        )
    }
    if (showAddDialog) AddDialog { showAddDialog = false }
}
//add new waypoint dialog
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
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = group, onValueChange = { group = it }, label = { Text("Group") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        //add new waypoint to the waypoint managers list
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
//dialog when deleting item to make sure
@Composable
fun DeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Waypoint") },
        text = { Text("you sure?") },
        confirmButton = { Button(onClick = onConfirm) { Text("Yes") } },
        dismissButton = { Button(onClick = onDismiss) { Text("No") } }
    )
}
//edit waypoint dialog
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EditDialog(item: WayPoint, onDismiss: () -> Unit, onConfirm: (String, WayPoint) -> Unit) {
    var newName by remember { mutableStateOf(item.name) }
    var newGroup by remember { mutableStateOf(item.group) }
    var newLong by remember { mutableDoubleStateOf(item.location.longitude) }
    var newLat by remember { mutableDoubleStateOf(item.location.latitude) }
    var oldName = remember { item.name }
    val groups = WayPointManager.groups
    var groupSelectExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Waypoint") },
        text = {
            Column {
                //editable fields
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                //for existing groups
                ExposedDropdownMenuBox(
                    expanded = groupSelectExpanded,
                    onExpandedChange = {groupSelectExpanded = !groupSelectExpanded})
                {
                    TextField(
                        value = newGroup,
                        onValueChange = { newGroup = it },
                        label = { Text("Group") },
                        trailingIcon = {
                            Icon(
                                imageVector = if (groupSelectExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.clickable { groupSelectExpanded = !groupSelectExpanded }
                            )
                            DropdownMenu(
                                expanded = groupSelectExpanded,
                                onDismissRequest = { groupSelectExpanded = false },
                            ) {
                                groups.forEach { group ->
                                    DropdownMenuItem(
                                        onClick = {
                                            newGroup = group
                                            groupSelectExpanded = false
                                        },
                                        text = { Text(group) }
                                    )
                                }
                            }
                        }
                    )
                }
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
                    //write edited item into updated item when confirm is clicked
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
