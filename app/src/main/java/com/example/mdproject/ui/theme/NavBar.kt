package com.example.mdproject.ui.theme

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mdproject.CompassActivity
import com.example.mdproject.GPSActivity
import com.example.mdproject.MapActivity
import com.example.mdproject.NavBarContents

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar() {
    var context = LocalContext.current
    val destinations = listOf(
        NavBarContents.Compass,
        NavBarContents.Level,
        NavBarContents.Gps
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        destinations.forEach { screen ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        when (screen) {
                            NavBarContents.Compass -> {
                                val intent = Intent(context, CompassActivity::class.java)
                                context.startActivity(intent)
                            }
                            NavBarContents.Level -> {
                                val intent = Intent(context, MapActivity::class.java)
                                context.startActivity(intent)
                            }
                            NavBarContents.Gps -> {
                                val intent = Intent(context, GPSActivity::class.java)
                                context.startActivity(intent)
                            }
                        }
                    }
            ) {
                Icon(imageVector = screen.icon, contentDescription = "Icon")
                Text(text = screen.title)
            }
        }
    }
}