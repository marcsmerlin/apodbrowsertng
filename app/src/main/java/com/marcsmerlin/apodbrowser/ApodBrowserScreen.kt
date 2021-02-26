package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ApodBrowserScreen(
    apodResult: State<ApodResult>,
    bitmapLoader: IBitmapLoader,
) {

    when (val value = apodResult.value) {
        ApodLoading -> ApodLoadingScreen()
        is ApodError -> ApodErrorScreen(error = value)
        is ApodSuccess ->
            ApodSuccess(
                success = value,
                bitmapLoader = bitmapLoader,
            )
    }
}

@Composable
private fun ApodLoadingScreen() {
    Text(text = "The ApodBrowser app is loading ...")
}

@Composable
private fun ApodErrorScreen(error: ApodError) {
    Text(text = "The ApodBrowser app has failed with error: $error")
}

@Composable
private fun ApodSuccess(
    success: ApodSuccess,
    bitmapLoader: IBitmapLoader,
) {
    val apod = success.apod

    if (apod.isImage()) {
        ApodImageTracker(
            bitmapLoader.queueRequest(apod.url),
            apod.contentDescription,
        )
    } else {
        ApodDescription(
            apod = apod,
        )
    }
}

@Composable
private fun ApodImageTracker(
    bitmapStatus: State<BitmapStatus>,
    contentDescription: String,
) {

    when (val value = bitmapStatus.value) {
        BitmapStatus.Loading ->
            Text(
                text = "Loading image ... "
            )
        is BitmapStatus.Error ->
            Text(
                text = "An error has occurred loading image: ${value.error}"
            )
        is BitmapStatus.Success ->
            ApodImage(
                bitmap = value.bitmap.asImageBitmap(),
                contentDescription = contentDescription,
            )
    }
}

@Composable
private fun ApodImage(
    bitmap: ImageBitmap,
    contentDescription: String,
) {
    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {},
            dismissButton = {},
            text = {
                Text(contentDescription)
            },
        )

    } else {
        Box(
            Modifier
                .fillMaxSize()
                .clickable(onClick = { openDialog.value = true })
        ) {
            Image(
                bitmap = bitmap,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ApodDescription(
    apod: Apod
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        with(apod) {
            Text(title)
            Text(date)
            if (hasCopyrightInfo())
                Text(copyrightInfo)
            Text(explanation, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun ApodDebugText(
    apod: Apod,
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        with(apod) {
            Text(title)
            Text(date)
            Text(mediaType)
            Text(url)
            if (hasCopyrightInfo())
                Text(copyrightInfo)
            Text(explanation, overflow = TextOverflow.Ellipsis)
        }
    }
}