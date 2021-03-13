package com.marcsmerlin.randomapod.utils

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
fun BitmapDownloadTracker(
    bitmapLoaderStatus: State<BitmapLoader.Status>,
    loadingContent: @Composable (String) -> Unit = { _ ->  },
    failedContent: @Composable (String, Exception) -> Unit = { _, _ ->  },
    doneContent: @Composable (String, Bitmap) -> Unit,
) {
    when (val value = bitmapLoaderStatus.value) {

        is BitmapLoader.Status.Loading -> {
            loadingContent(value.url)
        }

        is BitmapLoader.Status.Failed -> {
            failedContent(value.url, value.error)
        }

        is BitmapLoader.Status.Done -> {
            doneContent(value.url, value.bitmap)
        }
    }
}