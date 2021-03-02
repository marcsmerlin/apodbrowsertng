package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
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
import com.marcsmerlin.apodbrowser.utils.BitmapStatus
import com.marcsmerlin.apodbrowser.utils.IBitmapLoader

@Composable
fun HomeScreen(
    appName: String,
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
                appName = appName,
                result = result,
                goHome = { goHome() },
                getRandom = { getRandom() },
                getDetail = { getDetail() }
            )
        },
        content = {
            ScaffoldContent(
                apod = result.apod,
                bitmapLoader = bitmapLoader,
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
    getDetail: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = appName) },
        navigationIcon = {
            IconButton(onClick = { goHome() }, enabled = !result.isHome) {
                val contentDescription = "Go home"
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
        },
        actions = {
            IconButton(onClick = { getRandom() }) {
                val contentDescription = "Get random APOD"
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = contentDescription
                )
            }
            IconButton(onClick = { getDetail() }) {
                val contentDescription = "Get APOD detail"
                Icon(
                    Icons.Filled.Info,
                    contentDescription = contentDescription
                )
            }
        })
}

@Composable
private fun ScaffoldContent(
    apod: Apod,
    bitmapLoader: IBitmapLoader,
) {
    val label = "${apod.title} (${apod.date})"

    if (apod.isImage()) {
        LabeledContent(label = label) {
            BitmapTracker(
                bitmapStatus = bitmapLoader.queueRequest(apod.url),
            )
        }
    } else {
        LabeledContent(label = label) {
            UnsupportedMediaTypeNotice(
                mediaType = apod.mediaType,
            )
        }
    }
}

@Composable
private fun LabeledContent(
    label: String,
    contentToLabel: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        contentToLabel()
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .background(MaterialTheme.colors.surface.copy(alpha = 0.67f))
        ) {
            Text(
                text = label,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
private fun BitmapTracker(
    bitmapStatus: State<BitmapStatus>,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val value = bitmapStatus.value) {
            BitmapStatus.Loading ->
                Text(text = "Loading image\u2026")

            is BitmapStatus.Error ->
                Text(text = "An error has occurred loading bitmap:\n${value.error}")

            is BitmapStatus.Success ->
                Image(
                    bitmap = value.bitmap.asImageBitmap(),
                    contentDescription = "",
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
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "The media type \"$mediaType\" is not supported.",
        )
    }
}