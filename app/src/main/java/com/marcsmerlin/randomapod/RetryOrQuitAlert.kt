package com.marcsmerlin.randomapod

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlin.system.exitProcess

private val onDismissRequest = { exitProcess(1) }
private const val advisoryText = "Click Quit to close the app or Retry to try again."

@Composable
fun RetryOrQuitAlert(
    error: Exception,
    alertCause: String,
    modifier: Modifier = Modifier,
) {
    val onRetryRequest = LocalOnRetryRequest.current

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
                text = "$alertCause $advisoryText",
            )
        },
    )
}

@Preview
@Composable
fun PreviewRetryOrQuitDialog() {
    RetryOrQuitAlert(
        alertCause = "An error has occurred accessing the Apod Archive.",
        error = IllegalArgumentException(),
    )
}
