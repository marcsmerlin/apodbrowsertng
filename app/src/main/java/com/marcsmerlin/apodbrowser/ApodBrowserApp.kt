package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import com.marcsmerlin.apodbrowser.utils.BitmapStatus
import com.marcsmerlin.apodbrowser.utils.IBitmapLoader

@Composable
fun ApodBrowserScreen(
    apodStatus: State<ApodStatus>,
    bitmapLoader: IBitmapLoader,
) {

    when (val status = apodStatus.value) {

        ApodLoading ->
            SnackbarNotification(text = "Loading app \u2026")
        is ApodError ->
            ErrorAlertDialog(
                title = "Error loading app",
                error = status.error
            )
        is ApodSuccess ->
            ApodContentScreen(
                apod = status.apod,
                bitmapLoader = bitmapLoader,
            )
    }
}

@Composable
private fun ApodContentScreen(
    apod: Apod,
    bitmapLoader: IBitmapLoader,
) {

    if (apod.isImage()) {
        ApodImageTracker(
            bitmapLoader.queueRequest(apod.url),
        )
    } else {
        ApodTextDescription(
            apod = apod,
        )
    }
}

@Composable
private fun ApodImageTracker(
    bitmapStatus: State<BitmapStatus>,
) {

    when (val value = bitmapStatus.value) {

        BitmapStatus.Loading ->
            SnackbarNotification(text = "Loading image \u2026")
        is BitmapStatus.Error ->
            ErrorAlertDialog(
                title = "Error loading image",
                value.error,
            )
        is BitmapStatus.Success ->
            ApodImage(
                bitmap = value.bitmap.asImageBitmap(),
            )
    }
}

@Composable
private fun ApodImage(
    bitmap: ImageBitmap,
) {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ApodTextDescription(
    apod: Apod
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        with(apod) {
            Text(text = title)
            Text(text = date)
            if (hasCopyrightInfo()) Text(text = copyrightInfo)
            Text(
                text = explanation,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun SnackbarNotification(text: String) {
    Snackbar {
        Text(text = text)
    }
}

@Composable
private fun ErrorAlertDialog(
    title: String,
    error: Exception,
) {

    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = error.toString())
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("OK")
                }
            },
        )
    }
}