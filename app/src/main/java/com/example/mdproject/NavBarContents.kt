package com.example.mdproject

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavBarContents(val title: String, val icon: ImageVector){
    object Compass : NavBarContents(
        title = "Compass", icon = Icons.Default.AddCircle
    )
    object Level : NavBarContents(
        title = "Map", icon = Icons.Default.Place
    )
    object Gps : NavBarContents(
        title = "Gps", icon = Icons.Default.Menu
    )

}
