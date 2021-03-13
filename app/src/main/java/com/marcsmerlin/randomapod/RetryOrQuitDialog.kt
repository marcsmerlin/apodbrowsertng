package com.marcsmerlin.randomapod

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlin.system.exitProcess

@Composable
fun RetryOrQuitDialog(
    cause: String,
    error: Exception,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { exitProcess(1) },
    onRetryRequest: () -> Unit = {},
) {
    val advisoryText = "Click Quit to close the app or Retry to try again."

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onRetryRequest) {
                Text(text = "Retry")
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Quit")
            }
        },
        title = { Text(text = "$error") },
        text = {
            Text(
                text = "$cause $advisoryText",
            )
        },
    )
}

@Preview
@Composable
fun PreviewRetryOrQuitDialog() {
    RetryOrQuitDialog(
        cause = "An error has occurred accessing the Apod Archive.",
        error = IllegalArgumentException(),
    )
}
