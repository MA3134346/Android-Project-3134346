package com.example.mdproject.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mdproject.NavBarContents

@Composable
fun NavBarGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = NavBarContents.Compass.route){
        composable(route = NavBarContents.Compass.route){
            Compass()
        }
        composable(route = NavBarContents.Level.route){
            Level()
        }
        composable(route = NavBarContents.Gps.route){
            GPS()
        }
    }
}