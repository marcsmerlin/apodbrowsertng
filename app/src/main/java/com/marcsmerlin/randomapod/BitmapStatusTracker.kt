package com.marcsmerlin.randomapod

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marcsmerlin.randomapod.utils.BitmapStatus

@Composable
fun BitmapStatusTracker(
    bitmapStatus: State<BitmapStatus>
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        when (val value = bitmapStatus.value) {
            is BitmapStatus.Loading -> {
                Text(
                    text = "Downloading image from ${value.url}\u2026",
                    textAlign = TextAlign.Center,
                )
            }

            is BitmapStatus.Error -> {
                Text(
                    text = "Error downloading image from ${value.url}:\n${value.error}",
                    textAlign = TextAlign.Center,
                )
            }

            is BitmapStatus.Success -> {
                val zoomIn = remember { mutableStateOf(true) }

                Box(
                    modifier = Modifier.clickable { zoomIn.value = !zoomIn.value }
                ) {
                    val contentDescription = "Image downloaded from ${value.url}"

                    if (zoomIn.value) {
                        Image(
                            bitmap = value.bitmap.asImageBitmap(),
                            contentDescription = contentDescription,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Image(
                            bitmap = value.bitmap.asImageBitmap(),
                            contentDescription = contentDescription,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 12.dp, end = 12.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }
        }
    }
}