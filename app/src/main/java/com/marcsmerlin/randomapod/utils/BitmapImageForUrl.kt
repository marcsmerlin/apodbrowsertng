package com.marcsmerlin.randomapod.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BitmapImageForUrl(
    url: String,
    bitmapLoader: IBitmapLoader,

    ) {
    val bitmapStatus = remember(url) { bitmapLoader.queueRequest(url) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        when (val value = bitmapStatus.value) {
            BitmapStatus.Loading ->
                Text(
                    text = "Loading image\u2026",
                    textAlign = TextAlign.Center,
                )

            is BitmapStatus.Error ->
                Text(
                    text = "Error downloading image:\n${value.error}",
                    textAlign = TextAlign.Center,
                )

            is BitmapStatus.Success -> {
                Image(
                    bitmap = value.bitmap.asImageBitmap(),
                    contentDescription = "Image downloaded from $url",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}