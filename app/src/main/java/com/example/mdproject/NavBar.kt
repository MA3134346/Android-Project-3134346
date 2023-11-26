package com.example.mdproject

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


@Composable
fun NavBar() {
    //list of items for the navigation bar
    val destinations = listOf(
        NavBarContents.Compass,
        NavBarContents.Level,
        NavBarContents.Gps
    )
    //context for starting activities
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //iterate over each navigation destination
        destinations.forEach { screen ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        //which activity to start based on the selected button
                        val activityClass = when (screen) {
                            NavBarContents.Compass -> CompassActivity::class.java
                            NavBarContents.Level -> MapActivity::class.java
                            NavBarContents.Gps -> GPSActivity::class.java
                        }
                        //start the selected activity
                        context.startActivity(Intent(context, activityClass))
                    }
            ) {
                //icon and text for each item in the navbar
                Icon(imageVector = screen.icon, contentDescription = "Icon")
                Text(text = screen.title)
            }
        }
    }
}