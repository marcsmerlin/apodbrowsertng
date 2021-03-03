package com.marcsmerlin.apod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.marcsmerlin.apod.utils.BitmapStatus
import com.marcsmerlin.apod.utils.IBitmapLoader

@Composable
fun HomeScreen(
    appTitle: String,
    result: ApodModelView.Result,
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
    result: ApodModelView.Result,
    goHome: () -> Unit,
    getRandom: () -> Unit,
    getDetail: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = appName) },
        navigationIcon = {
            IconButton(onClick = { getDetail() }) {
                val contentDescription = "Show APOD detail"
                Icon(
                    Icons.Filled.Info,
                    contentDescription = contentDescription
                )
            }
        },
        actions = {
            IconButton(onClick = { getRandom() }) {
                val contentDescription = "Fetch random APOD"
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
) {
    val label = "${apod.title} (${apod.date})"

    if (apod.isImage()) {
        ContentWithLabel(label = label) {
            BitmapDownloadTracker(bitmapStatus = bitmapLoader.queueRequest(apod.url))
        }
    } else {
        ContentWithLabel(label = label) {
            UnsupportedMediaTypeNotice(mediaType = apod.mediaType)
        }
    }
}

@Composable
private fun ContentWithLabel(
    label: String,
    contentToLabel: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        contentToLabel()
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .background(
                    color = MaterialTheme.colors.surface.copy(alpha = 0.40f),
                    shape = RoundedCornerShape(8.dp),
                ),
        ) {
            Text(
                text = label,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(all = 6.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
private fun BitmapDownloadTracker(
    bitmapStatus: State<BitmapStatus>,
) {
    when (val value = bitmapStatus.value) {
        BitmapStatus.Loading ->
            Text(text = "Loading image\u2026")

        is BitmapStatus.Error ->
            Text(text = "Error downloading image:\n${value.error}")

        is BitmapStatus.Success ->
            Image(
                bitmap = value.bitmap.asImageBitmap(),
                contentDescription = "Download image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
    }
}

@Composable
private fun UnsupportedMediaTypeNotice(
    mediaType: String
) {
    Text(text = "The media type \"$mediaType\" is not supported.",)
}