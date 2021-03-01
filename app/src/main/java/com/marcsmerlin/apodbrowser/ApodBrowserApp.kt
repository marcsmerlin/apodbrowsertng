package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.system.exitProcess

@Composable
fun ApodBrowserApp(
    appContainer: AppContainer,
    viewModel: ApodViewModel,
) {
    when (val status = viewModel.status.value) {
        ApodViewModel.Status.Initializing -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Initializing\u2026")
            }
        }
        is ApodViewModel.Status.Failed -> {
            UnrecoverableErrorAlert(
                title = "An unrecoverable error has occurred",
                status.error
            )
        }
        is ApodViewModel.Status.Operational ->
            HomeScreen(
                result = viewModel.requestResult.value,
                bitmapLoader = appContainer.bitmapLoader,
                goHome = viewModel::goHome,
                getRandom = viewModel::getRandom,
            )
    }
}

@Composable
private fun UnrecoverableErrorAlert(
    title: String,
    error: Exception,
) {
    AlertDialog(
        onDismissRequest = { exitProcess(1) },
        title = {
            Text(text = title)
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