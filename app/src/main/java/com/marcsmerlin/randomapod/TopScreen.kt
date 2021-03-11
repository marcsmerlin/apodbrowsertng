package com.marcsmerlin.randomapod

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcsmerlin.randomapod.utils.BitmapLoader
import kotlin.system.exitProcess

@Composable
fun TopScreen(
    appName: String,
    bitmapLoader: BitmapLoader,
    viewModel: ApodViewModel,
) {
    when (val value = viewModel.status.value) {

        ApodViewModel.Status.Initializing ->
            InitializingScreen(appName)

        is ApodViewModel.Status.InitializationFailure ->
            InitializationFailureAlert(
                appName,
                value.error,
            )

        ApodViewModel.Status.Operational ->
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
private fun InitializationFailureAlert(
    appName: String,
    error: Exception
) {
    val dismissAction = { exitProcess(1) }

    AlertDialog(
        onDismissRequest = dismissAction,
        confirmButton = {
            TextButton(
                onClick = dismissAction,
            ) {
                Text("Confirm")
            }
        },
        modifier = Modifier.fillMaxSize(),
        title = {
            Text(
                text = "Initialization has failed for $appName"
            )
        },
        text = {
            Text( text =
            "An error has occurred during app initialization. " + "" +
                    "Click the \"Confirm\" button to close the app.\n" + error)
        }
    )
}

@Composable
private fun OperationalScreen(
    appName: String,
    bitmapLoader: BitmapLoader,
    viewModel: ApodViewModel,
) {
    val navHostController = rememberNavController()

    @Composable
    fun GoBackTopBar() {
        TopAppBar(
            title = { Text(text = appName) },
            navigationIcon = {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Return to previous screen",
                    )
                }
            })
    }

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
                viewModel = viewModel,
                topBar = { GoBackTopBar() },
            )
        }
        composable(route = "about") {
            AboutScreen(
                topBar = { GoBackTopBar() },
            )
        }
        composable(route = "credits") {
            CreditsScreen(
                topBar = { GoBackTopBar() },
            )
        }
    }
}