package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ApodBrowserScreen(
    result: State<ApodResult>,
    getRandom: () -> Unit,
) {

    when (val value = result.value) {
        is ApodError -> ApodErrorScreen(error = value)
        ApodLoading -> ApodLoadingScreen()
        is ApodSuccess ->
            ApodSuccessScreen(
                success = value,
                getRandom = getRandom,
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
private fun ApodSuccessScreen(
    success: ApodSuccess,
    getRandom: () -> Unit,
) {
    val apod = success.apod

    if (apod.isImage()) {
        ApodTextScreen(
            apod = apod,
            getRandom = getRandom,
        )
    } else {
        ApodTextScreen(
            apod = apod,
            getRandom = getRandom
        )
    }
}

@Composable
private fun ApodTextScreen(
    apod: Apod,
    getRandom: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .clickable(onClick = getRandom)
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