package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
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
import com.marcsmerlin.apodbrowser.utils.BitmapStatus
import com.marcsmerlin.apodbrowser.utils.IBitmapLoader

@Composable
fun ApodBrowserApp(
    appContainer: AppContainer,
    viewModel: ApodViewModel,
) {
    when (val status = viewModel.status.value) {
        ApodViewModel.Status.Initializing -> {
            Text(text = "Initializing app\u2026")
        }
        is ApodViewModel.Status.FailedToInitialize -> {
            Text(text = "Initialization error: $status.error")
        }
        is ApodViewModel.Status.Result ->
            OperationalScreen(
                result = status,
                bitmapLoader = appContainer.bitmapLoader,
                goHome = viewModel::goHome,
                getRandom = viewModel::getRandom,
            )
    }
}

@Composable
private fun OperationalScreen(
    result: ApodViewModel.Status.Result,
    bitmapLoader: IBitmapLoader,
    goHome: () -> Unit,
    getRandom: () -> Unit,
) {
    when (result) {
        is ApodViewModel.Status.Result.Error ->
            Text(text = "Operational error: $result.error")

        is ApodViewModel.Status.Result.Success -> {
            val scaffoldState = rememberScaffoldState()

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBarForSuccess(
                        result = result,
                        title = "Apod Browser",
                        goHome = { goHome() },
                        getRandom = { getRandom() })
                },
                content = {
                    ApodContent(
                        apod = result.apod,
                        bitmapLoader = bitmapLoader,
                    )
                },
            )
        }
    }

}

@Composable
private fun TopAppBarForSuccess(
    result: ApodViewModel.Status.Result.Success,
    title: String,
    goHome: () -> Unit,
    getRandom: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = title) },
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
        }
    )
}

@Composable
private fun ApodContent(
    apod: Apod,
    bitmapLoader: IBitmapLoader,
) {
    Box {
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
                .padding(bottom = 24.dp)
                .background(backgroundColor.copy(alpha = 0.50f))
        ) {
            Text(
                text = "${apod.title} (${apod.date})",
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
private fun UnsupportedMediaType(
    apod: Apod
) {
    Column {
        Text(text = "(Media type \"${apod.mediaType}\" is not yet supported.)")
        Spacer(modifier = Modifier.padding(12.dp))
        Text(text = "${apod.title} (${apod.date})")
        if (apod.hasCopyrightInfo()) Text(text = "Credit: ${apod.copyrightInfo}")
        Text(
            text = apod.explanation,
            overflow = TextOverflow.Ellipsis,
        )
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