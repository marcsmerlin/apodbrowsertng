package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
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
import kotlin.system.exitProcess

@Composable
fun ApodBrowserApp(
    appContainer: AppContainer,
    viewModel: ApodViewModel,
) {
    when (val status = viewModel.status.value) {
        ApodViewModel.Status.Initializing -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Initializing\u2026")
            }
        }
        is ApodViewModel.Status.Failed -> {
            FatalErrorAlert(
                title = "An unrecoverable error has occurred",
                status.error
            )
        }
        is ApodViewModel.Status.Operational ->
            HomeScreen(
                result = viewModel.requestResult.value,
                bitmapLoader = appContainer.bitmapLoader,
                goHome = viewModel::goHome,
                getRandom = viewModel::getRandom,
            )
    }
}

@Composable
private fun HomeScreen(
    result: ApodViewModel.Result,
    bitmapLoader: IBitmapLoader,
    goHome: () -> Unit,
    getRandom: () -> Unit,
) {

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            HomeScreenTopAppBar(
                result = result,
                goHome = { goHome() },
                getRandom = { getRandom() },
                getInfo = {}
            )
        },
        content = {
            HomeScreenContent(
                apod = result.apod,
                bitmapLoader = bitmapLoader,
            )
        },
    )
}

@Composable
private fun HomeScreenTopAppBar(
    result: ApodViewModel.Result,
    goHome: () -> Unit,
    getRandom: () -> Unit,
    getInfo: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Apod Browser") },
        actions = {
            IconButton(onClick = { goHome() }, enabled = !result.isHome) {
                if (!result.isHome)
                    Icon(
                        Icons.Filled.Home,
                        "Go home"
                    )
                else
                    Icon(
                        Icons.Filled.Home,
                        "Go home",
                        tint = Color.DarkGray
                    )
            }
            IconButton(onClick = { getRandom() }) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = "Get random photo"
                )
            }
            IconButton(onClick = { getInfo() }) {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = "Get image detail"
                )
            }
        })
}

@Composable
private fun HomeScreenContent(
    apod: Apod,
    bitmapLoader: IBitmapLoader,
) {
    val label = "${apod.title} (${apod.date})"

    if (apod.isImage()) {
        LabeledContent(label = label)
        {
            ImageMediaType(
                bitmapStatus = bitmapLoader.queueRequest(apod.url),
            )
        }

    } else {
        LabeledContent(label = label)
        {
            UnsupportedMediaType(
                mediaType = apod.mediaType,
            )
        }
    }
}

@Composable
private fun LabeledContent(
    label: String,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .background(backgroundColor.copy(alpha = 0.50f))
        ) {
            Text(
                text = label,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Yellow,
            )
        }
    }
}

@Composable
private fun ImageMediaType(
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
                FatalErrorAlert(
                    title = "An error has occurred loading the image",
                    value.error,
                )
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
private fun UnsupportedMediaType(
    mediaType: String
) {
    Box {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = "The media type \"$mediaType\" is not supported.",
            )
        }
    }
}

@Composable
private fun FatalErrorAlert(
    title: String,
    error: Exception,
) {
    AlertDialog(
        onDismissRequest = { exitProcess(1) },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = error.toString())
        },
        confirmButton = {
            TextButton(onClick = { exitProcess(1) }
            ) {
                Text(text = "Quit")
            }
        },
    )
}