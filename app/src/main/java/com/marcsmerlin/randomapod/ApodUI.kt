package com.marcsmerlin.randomapod

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcsmerlin.randomapod.utils.IBitmapLoader

/*
ApodUI: Top-level Composable used to couple resources supplied from the app's MainActivity to its
user interface.
 */
@Composable
fun ApodUI(
    appName: String,
    bitmapLoader: IBitmapLoader,
    viewModel: ApodViewModel,
) {
    when (viewModel.status.value) {

        ApodViewModel.Status.Initializing ->
            InitializingScreen(appName)

        is ApodViewModel.Status.Operational ->
            OperationalScreen(
                appName,
                bitmapLoader,
                viewModel,
            )
    }
}

@Composable
private fun InitializingScreen(
    appName: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Initializing $appName\u2026")
    }
}

@Composable
private fun OperationalScreen(
    appName: String,
    bitmapLoader: IBitmapLoader,
    viewModel: ApodViewModel,
) {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = "home",
    ) {
        composable(route = "home") {
            HomeScreen(
                appName = appName,
                bitmapLoader = bitmapLoader,
                viewModel = viewModel,
                navHostController = navHostController,
            )
        }
        composable(route = "detail") {
            DetailScreen(
                appName = appName,
                viewModel = viewModel,
                goBack = { navHostController.popBackStack() },
            )
        }
        composable(route = "about") {
            AboutScreen(
                appName = appName,
                goBack = { navHostController.popBackStack() },
            )
        }
        composable(route = "credits") {
            CreditsScreen(
                appName = appName,
                goBack = { navHostController.popBackStack() },
            )
        }
    }
}