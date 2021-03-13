package com.marcsmerlin.randomapod

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcsmerlin.randomapod.utils.BitmapDownloadTracker
import com.marcsmerlin.randomapod.utils.BitmapLoader

@Composable
fun ApodContent(
    apod: Apod,
    goToDetail: () -> Unit,
) {
    val bitmapLoader = LocalBitmapLoader.current

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        when (apod.mediaType) {
            "image" -> {
                ApodBitmapDownloadTracker(
                    bitmapLoaderStatus = bitmapLoader.queueRequest(apod.url),
                )
            }
            "video" -> {
                if (apod.hasThumbnail()) {
                    ApodBitmapDownloadTracker(
                        bitmapLoaderStatus = bitmapLoader.queueRequest(apod.thumbnailUrl),
                    )
                } else {
                    NoThumbnailAvailableForVideoNotice()
                }
            }
            else -> {
                UnsupportedMediaTypeNotice(mediaType = apod.mediaType)
            }
        }

        val overlayBackground = MaterialTheme.colors.surface.copy(alpha = 0.66f)

        Text(
            text = "${apod.title} (${apod.date})",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .background(
                    color = overlayBackground,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
        )

        FloatingActionButton(
            onClick = { goToDetail() },
            backgroundColor = overlayBackground,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 18.dp,
                    start = 8.dp
                )
                .size(42.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Go to detail screen",
            )
        }
    }
}

@Composable
private fun ApodBitmapDownloadTracker(
    bitmapLoaderStatus: State<BitmapLoader.Status>,
) {
    BitmapDownloadTracker(
        bitmapLoaderStatus = bitmapLoaderStatus,

        loadingContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray)
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        },

        failedContent = { url, error ->
            val alertCause = "Error downloading image for $url:\n"

            RetryOrQuitAlert(
                error = error,
                alertCause = alertCause,
            )
        },

        doneContent = { url, bitmap ->
            ApodBitmap(url, bitmap)
        },
    )
}

@Composable
private fun ApodBitmap(
    url: String,
    bitmap: Bitmap,
) {
    val zoomIn = remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { zoomIn.value = !zoomIn.value }
    ) {
        val contentDescription = "Image downloaded for $url"

        if (zoomIn.value) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = contentDescription,
                modifier = Modifier
                    .matchParentSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = contentDescription,
                modifier = Modifier
                    .matchParentSize()
                    .padding(all = 12.dp),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
private fun UnsupportedMediaTypeNotice(
    mediaType: String
) {
    val cause = "Sorry, the media type \"$mediaType\" is not yet supported by this app."
    ApodTextDescriptionAvailableNotice(cause = cause)
}

@Composable
private fun NoThumbnailAvailableForVideoNotice(
) {
    val cause = "Sorry, there is no thumbnail available to display for this video media."
    ApodTextDescriptionAvailableNotice(cause = cause)
}

@Composable

private fun ApodTextDescriptionAvailableNotice(
    cause: String
) {
    val advisoryText = "Click on the floating info button above for a text description of this Apod."

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, end = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$cause $advisoryText",
            textAlign = TextAlign.Center,
        )
    }
}