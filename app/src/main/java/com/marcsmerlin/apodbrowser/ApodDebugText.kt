package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

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