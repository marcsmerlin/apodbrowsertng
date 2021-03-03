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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.marcsmerlin.apodbrowser.utils.IBitmapLoader
import kotlin.system.exitProcess

@Composable
fun ApodBrowserUI(
    modelViewImpl: ApodModelViewImpl,
    bitmapLoader: IBitmapLoader,
) {
    val appTitle = stringResource(id = R.string.app_title)

    when (val status = modelViewImpl.status.value) {

        ApodModelView.Status.Initializing -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Initializing $appTitle\u2026")
            }
        }
        is ApodModelView.Status.Failed -> {
            UnrecoverableErrorAlert(
                status.error
            )
        }
        is ApodModelView.Status.Operational -> {

            ApodBrowserNavigator(
                modelViewImpl = modelViewImpl,
                bitmapLoader = bitmapLoader,
                appTitle = appTitle,
            )
        }
    }
}

@Composable
private fun ApodBrowserNavigator(
    modelViewImpl: ApodModelView,
    bitmapLoader: IBitmapLoader,
    appTitle: String,
) {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = "home",
    ) {

        composable("home") {
            HomeScreen(
                appTitle = appTitle,
                result = modelViewImpl.requestResult.value,
                bitmapLoader = bitmapLoader,
                goHome = modelViewImpl::goHome,
                getRandom = modelViewImpl::getRandom,
                getDetail = { navHostController.navigate("detail") },
            )
        }

        composable("detail") {
            DetailScreen(
                appTitle = appTitle,
                apod = modelViewImpl.requestResult.value.apod,
                goBack = { navHostController.popBackStack() }
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