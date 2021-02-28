package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcsmerlin.apodbrowser.ui.theme.ApodBrowserTheme
import com.marcsmerlin.apodbrowser.utils.BitmapStatus
import com.marcsmerlin.apodbrowser.utils.IBitmapLoader

@Composable
fun ApodBrowserApp(
    appContainer: AppContainer,
    apodViewModel: IApodViewModel,
) {
    ApodBrowserTheme {
        HomeScaffold(
            apodStatus = apodViewModel.status,
            bitmapLoader = appContainer.bitmapLoader
        )
    }
}

@Composable
private fun HomeScaffold(
    apodStatus: State<ApodStatus>,
    bitmapLoader: IBitmapLoader,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            val title = "Apod Browser"
            TopAppBar(
                title = { Text(text = title) },
            )
        },
        content = {
            ScaffoldContent(
                apodStatus = apodStatus,
                bitmapLoader = bitmapLoader,
            )
        },
    )
}

@Composable
private fun ScaffoldContent(
    apodStatus: State<ApodStatus>,
    bitmapLoader: IBitmapLoader,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val status = apodStatus.value) {
            ApodLoading ->
                Text(text = "Loading app\u2026")
            is ApodError ->
                ErrorAlert(
                    title = "Error loading app",
                    error = status.error
                )
            is ApodSuccess ->
                ApodContent(
                    apod = status.apod,
                    bitmapLoader = bitmapLoader,
                )
        }
    }
}

@Composable
private fun ApodContent(
    apod: Apod,
    bitmapLoader: IBitmapLoader,
) {
    Box {
        Box(
            Modifier.align(Alignment.TopCenter)
        ) {
            if (apod.isImage()) {
                ImageContent(
                    apod,
                    bitmapLoader.queueRequest(apod.url),
                )
            } else {
                UnsupportedMediaType(
                    apod = apod,
                )
            }
        }
    }
}

@Composable
private fun ImageContent(
    apod: Apod,
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
                ErrorAlert(
                    title = "Error loading image",
                    value.error,
                )
            is BitmapStatus.Success ->
                BitmapImageWithTitle(
                    apod,
                    bitmap = value.bitmap.asImageBitmap(),
                )
        }
    }
}

@Composable
private fun BitmapImageWithTitle(
    apod: Apod,
    bitmap: ImageBitmap,
) {
    Box {
        Box {
            Image(
                bitmap = bitmap,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            Modifier
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = "${apod.title} (${apod.date})",
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.Transparent)
                    .padding(bottom = 18.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Yellow,
            )
        }
    }
}

@Composable
private fun UnsupportedMediaType(
    apod: Apod
) {
    Column {
        with(apod) {
            Text(text = "The media type \"${apod.mediaType}\" is not yet supported")
            Text(text = title)
            Text(text = date)
            if (hasCopyrightInfo()) Text(text = copyrightInfo)
            Text(
                text = explanation,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ErrorAlert(
    title: String,
    error: Exception,
) {

    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = error.toString())
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("OK")
                }
            },
        )
    }
}