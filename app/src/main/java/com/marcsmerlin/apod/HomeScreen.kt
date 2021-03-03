package com.marcsmerlin.apod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcsmerlin.apod.utils.BitmapStatus
import com.marcsmerlin.apod.utils.IBitmapLoader

@Composable
fun HomeScreen(
    appTitle: String,
    result: ApodViewModel.Result,
    bitmapLoader: IBitmapLoader,
    goHome: () -> Unit,
    getRandom: () -> Unit,
    getDetail: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ScaffoldTopBar(
                appName = appTitle,
                result = result,
                goHome = { goHome() },
                getRandom = { getRandom() },
            )
        },
        content = {
            ScaffoldContent(
                apod = result.apod,
                bitmapLoader = bitmapLoader,
                getDetail = { getDetail() },
            )
        },
    )
}

@Composable
private fun ScaffoldTopBar(
    appName: String,
    result: ApodViewModel.Result,
    goHome: () -> Unit,
    getRandom: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = appName) },
        navigationIcon = {
            IconButton(
                onClick = {},
                enabled = false
            ) {
                val contentDescription = "Menu"
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = contentDescription
                )
            }
        },
        actions = {
            IconButton(onClick = { getRandom() }) {
                val contentDescription = "Get random APOD"
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = contentDescription
                )
            }
            IconButton(onClick = { goHome() }, enabled = !result.isHome) {
                val contentDescription = "Go to APOD home"
                if (!result.isHome)
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = contentDescription
                    )
                else
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = contentDescription,
                        tint = Color.DarkGray
                    )
            }
        })
}

@Composable
private fun ScaffoldContent(
    apod: Apod,
    bitmapLoader: IBitmapLoader,
    getDetail: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        if (apod.isImage()) {
            BitmapDownloadTracker(bitmapStatus = bitmapLoader.queueRequest(apod.url))
        } else {
            UnsupportedMediaTypeNotice(mediaType = apod.mediaType)
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
            onClick = { getDetail() },
            backgroundColor = overlayBackground,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 18.dp,
                    start = 8.dp
                )
                .size(42.dp)
        ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = "Show Apod detail",
            )
        }
    }
}

@Composable
private fun BitmapDownloadTracker(
    bitmapStatus: State<BitmapStatus>,
) {
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

            is BitmapStatus.Success ->
                Image(
                    bitmap = value.bitmap.asImageBitmap(),
                    contentDescription = "Download image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
        }
    }
}

@Composable
private fun UnsupportedMediaTypeNotice(
    mediaType: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "The media type \"$mediaType\" is not supported.",
            textAlign = TextAlign.Center,
        )
    }
}