package com.example.mdproject

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavBarContents(val route: String, val title: String, val icon: ImageVector){
    object Compass : NavBarContents(
        route = "compass", title = "Compass", icon = Icons.Default.AddCircle
    )
    object Level : NavBarContents(
        route = "level", title = "Level", icon = Icons.Default.Menu
    )
    object Gps : NavBarContents(
        route = "gps", title = "Gps", icon = Icons.Default.LocationOn
    )

}
