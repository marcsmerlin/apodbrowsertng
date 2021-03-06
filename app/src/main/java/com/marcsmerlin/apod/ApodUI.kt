package com.marcsmerlin.apod

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.marcsmerlin.apod.utils.IBitmapLoader

@Composable
fun ApodUI(
    bitmapLoader: IBitmapLoader,
    viewModel: ApodViewModel,
) {
    when (viewModel.status.value) {

        ApodViewModel.Status.Initializing ->
            InitializingScreen()

        is ApodViewModel.Status.Operational ->
            OperationalScreen(
                bitmapLoader,
                viewModel,
            )
    }
}

@Composable
private fun InitializingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Initializing\u2026")
    }
}

@Composable
private fun OperationalScreen(
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
                bitmapLoader = bitmapLoader,
                viewModel = viewModel,
                navHostController = navHostController,
            )
        }
        composable(route = "detail") {
            DetailScreen(
                viewModel = viewModel,
                goBack = { navHostController.popBackStack() },
            )
        }
        composable(route = "about") {
            AboutScreen(
                goBack = { navHostController.popBackStack() },
            )
        }
        composable(route = "credits") {
            CreditsScreen(
                goBack = { navHostController.popBackStack() },
            )
        }
    }
}