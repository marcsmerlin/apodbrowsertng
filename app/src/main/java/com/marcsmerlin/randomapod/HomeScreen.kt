package com.marcsmerlin.randomapod

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.marcsmerlin.randomapod.utils.BitmapLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@Composable
fun HomeScreen(
    appName: String,
    bitmapLoader: BitmapLoader,
    viewModel: ApodViewModel,
    navHostController: NavHostController,
) {
    MyScaffold(
        appName = appName,
        bitmapLoader = bitmapLoader,
        navHostController = navHostController,
        result = viewModel.result,
        isToday = viewModel.isToday,
        goToday = viewModel::goToday,
        getRandom = viewModel::getRandom,
    )
}

@Composable
private fun MyScaffold(
    appName: String,
    bitmapLoader: BitmapLoader,
    result: State<ApodViewModel.Result>,
    navHostController: NavHostController,
    isToday: State<Boolean>,
    goToday: () -> Unit,
    getRandom: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            MyDrawerContent(
                appName = appName,
                scaffoldState = scaffoldState,
                navHostController = navHostController,
                coroutineScope = coroutineScope,
            )
        },
        topBar = {
            MyTopBar(
                appName = appName,
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
                isToday = isToday,
                goToday = goToday,
                getRandom = getRandom,
            )
        },
        content = {
            MyContent(
                bitmapLoader = bitmapLoader,
                navHostController = navHostController,
                result = result,
                restart = goToday,
            )
        },
    )
}

@Composable
private fun MyDrawerContent(
    appName: String,
    scaffoldState: ScaffoldState,
    navHostController: NavHostController,
    coroutineScope: CoroutineScope,
) {
    @Composable
    fun MyMenuButton(
        text: String,
        route: String,
    ) {
        TextButton(
            onClick = {
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                    navHostController.navigate(route)
                }
            },
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.h5,
            )
        }
    }

    Column(
        Modifier.padding(start = 36.dp, top = 36.dp)
    ) {
        Text(
            text = appName,
            style = MaterialTheme.typography.h5,
            color = Color.LightGray,
        )
        Spacer(Modifier.padding(bottom = 18.dp))
        MyMenuButton(text = "About", route = "about")
        MyMenuButton(text = "Credits", route = "credits")
    }
}

@Composable
private fun MyTopBar(
    appName: String,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    isToday: State<Boolean>,
    goToday: () -> Unit,
    getRandom: () -> Unit,
) {
    @Composable
    fun MyActionButton(
        imageVector: ImageVector,
        contentDescription: String,
        onClick: () -> Unit,
        enabled: Boolean = true,
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
            )
        }
    }

    TopAppBar(
        title = { Text(text = appName) },

        navigationIcon = {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                })
            {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {

            MyActionButton(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Fetch random APOD",
                onClick = getRandom
            )

            MyActionButton(
                imageVector = Icons.Filled.Home,
                contentDescription = "Go to today's APOD",
                onClick = goToday,
                enabled = !isToday.value
            )
        })
}

@Composable
private fun MyContent(
    bitmapLoader: BitmapLoader,
    navHostController: NavHostController,
    result: State<ApodViewModel.Result>,
    restart: () -> Unit,

    ) {

    when (val value = result.value) {

        is ApodViewModel.Result.Data -> {
            ApodContent(
                bitmapLoader = bitmapLoader,
                apod = value.apod,
                goToDetail = { navHostController.navigate(route = "detail") },
                restart = restart
            )
        }

        is ApodViewModel.Result.Error -> {
            val alertTitle = "Error accessing Apod archive"
            val alertText = "Click on the \"Restart\" button below to restart or press \"Quit\" to close the app."

            ErrorAlert(
                title = alertTitle,
                text = alertText,
                error = value.error,
                restart = restart,
            )
        }
    }
}

@Composable
private fun BitmapLoaderTracker(
    bitmapLoaderStatus: State<BitmapLoader.Status>,
    restart: () -> Unit,
) {

    when (val value = bitmapLoaderStatus.value) {
        is BitmapLoader.Status.Loading -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray)
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is BitmapLoader.Status.Failed -> {
            val alertTitle = "Error downloading image"
            val alertText = "Error downloading image for ${value.url}:\n"

            ErrorAlert(
                title = alertTitle,
                text = alertText,
                error = value.error,
                restart = restart,
            )
        }

        is BitmapLoader.Status.Done -> {
            ApodImage(value.url, value.bitmap)
        }
    }
}

@Composable
private fun ApodImage(
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
private fun ApodContent(
    bitmapLoader: BitmapLoader,
    apod: Apod,
    goToDetail: () -> Unit,
    restart: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        when (apod.mediaType) {
            "image" -> {
                BitmapLoaderTracker(
                    bitmapLoaderStatus = bitmapLoader.queueRequest(apod.url),
                    restart = restart,
                )
            }
            "video" -> {
                if (apod.hasThumbnail()) {
                    BitmapLoaderTracker(
                        bitmapLoaderStatus = bitmapLoader.queueRequest(apod.thumbnailUrl),
                        restart = restart
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
private fun UnsupportedMediaTypeNotice(
    mediaType: String
) {
    val text = "Sorry, the media type \"$mediaType\" is not supported. Click on the floating info button above for a text description of this Apod."

    TextNotice(text = text)
}

@Composable
private fun NoThumbnailAvailableForVideoNotice(
) {
    val text = "Sorry, there is no thumbnail available to show for the video link provided. Click on the floating info button above for a text description of this Apod."

    TextNotice(text = text)
}

@Composable
private fun TextNotice(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, end = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ErrorAlert(
    title: String,
    text: String,
    error: Exception,
    restart: () -> Unit,
) {
    val onDismissRequest = { exitProcess(1) }

    Box (modifier = Modifier
        .fillMaxSize()
        .padding(start = 18.dp, end = 18.dp),
        contentAlignment = Alignment.Center
    ){
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = restart) {
                    Text(text = "Restart")
                }
            },
            modifier = Modifier
                .matchParentSize(),
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Quit")
                }
            },
            title = { Text(text = title) },
            text = { Text(text = "$text\n$error") },
        )
    }
}