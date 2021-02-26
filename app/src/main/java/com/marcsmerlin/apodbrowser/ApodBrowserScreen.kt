package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ApodBrowserScreen(
    result: State<ApodResult>,
    bitmapLoader: IBitmapLoader,
) {

    when (val value = result.value) {
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
    Text(text = "ApodBrowser is loading ...")
}

@Composable
private fun ApodErrorScreen(error: ApodError) {
    Text(text = "ApodBrowser has failed with error: ${error}.")
}

@Composable
private fun ApodSuccess(
    success: ApodSuccess,
    bitmapLoader: IBitmapLoader,
) {
    val apod = success.apod

    if (apod.isImage()) {
        ApodImage(
            bitmapLoader.queueRequest(apod.url),
        )
    } else {
        ApodText(
            apod = apod,
        )
    }
}

@Composable
private fun ApodImage(
    bitmapStatus: State<BitmapStatus>
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
            Image(
                bitmap = value.bitmap.asImageBitmap(),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
    }
}

@Composable
private fun ApodText(
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