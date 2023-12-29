package com.phantom.smplayer.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Home() {

    Box(
        modifier = Modifier
            .background(Color.Blue)
            .fillMaxSize()
    ) {
        val navController = rememberNavController()

        NavHost(
            navController,
            startDestination = "DirectoryList",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable("DirectoryList") { DirectoryList(navController = navController) }

            composable(
                "Directory/{directory}",
                arguments = listOf(navArgument("directory") { type = NavType.StringType })
            ) {
                val directory = it.arguments?.getString("directory") ?: ""
                Directory(navController = navController, directory = directory)
            }
        }
    }

}