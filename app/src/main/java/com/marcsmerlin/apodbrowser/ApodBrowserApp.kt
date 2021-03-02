package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
// import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import kotlin.system.exitProcess

@Composable
fun ApodBrowserApp(
    appContainer: AppContainer,
    viewModel: ApodViewModel,
) {
    val appName = stringResource(id = R.string.app_name)

    when (val status = viewModel.status.value) {
        ApodViewModel.Status.Initializing -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Initializing $appName\u2026")
            }
        }
        is ApodViewModel.Status.Failed -> {
            UnrecoverableErrorAlert(
                status.error
            )
        }
        is ApodViewModel.Status.Operational -> {

            ApodBrowserNavigator(
                appContainer = appContainer,
                viewModel = viewModel,
                appName = appName,
            )
        }
    }
}

@Composable
private fun ApodBrowserNavigator(
    appContainer: AppContainer,
    viewModel: ApodViewModel,
    appName: String,
) {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = "home",
    ) {

        composable("home") {
            HomeScreen(
                appName = appName,
                result = viewModel.requestResult.value,
                bitmapLoader = appContainer.bitmapLoader,
                goHome = viewModel::goHome,
                getRandom = viewModel::getRandom,
                getDetail = { navHostController.navigate("detail") },
            )
        }

        composable("detail") {
            DetailScreen(
                apod = viewModel.requestResult.value.apod,
            )
        }
    }
}

@Composable
private fun UnrecoverableErrorAlert(
    error: Exception,
) {
    AlertDialog(
        onDismissRequest = { exitProcess(1) },
        title = {
            Text(text = "An unrecoverable error has occurred")
        },
        text = {
            Text(text = error.toString())
        },
        confirmButton = {
            TextButton(onClick = { exitProcess(1) }
            ) {
                Text(text = "Quit")
            }
        },
    )
}