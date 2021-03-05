package com.marcsmerlin.apod

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.marcsmerlin.apod.utils.BitmapImageForUrl
import com.marcsmerlin.apod.utils.IBitmapLoader
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    bitmapLoader: IBitmapLoader,
    viewModel: ApodViewModel,
    navHostController: NavHostController,
) {
    val title = stringResource(id = R.string.app_name)

    HomeScaffold(
        bitmapLoader = bitmapLoader,
        result = viewModel.result.value,
        navHostController = navHostController,
        title = title,
        isHome = viewModel::isHome,
        goHome = viewModel::goHome,
        getRandom = viewModel::getRandom,
    )
}

@Composable
private fun HomeScaffold(
    bitmapLoader: IBitmapLoader,
    result: ApodViewModel.Result,
    navHostController: NavHostController,
    title: String,
    isHome: () -> Boolean,
    goHome: () -> Unit,
    getRandom: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Spacer(Modifier.height(24.dp))
            TextButton(
                onClick = {
                    navHostController.navigate("about")
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
            ) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.h5,
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = title) },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        })
                    {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = getRandom) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = "Get random APOD"
                        )
                    }

                    IconButton(
                        onClick = goHome,
                        enabled = !isHome()
                    ) {
                        val contentDescription = "Go to APOD home"

                        if (!isHome()) {
                            Icon(
                                Icons.Filled.Home,
                                contentDescription,
                            )
                        } else {
                            Icon(
                                Icons.Filled.Home,
                                contentDescription,
                                tint = Color.DarkGray,
                            )
                        }

                    }
                })
        },
        content = {
            when (result) {

                is ApodViewModel.Result.Data ->
                    ApodContent(
                        bitmapLoader = bitmapLoader,
                        apod = result.apod,
                        goToDetail = { navHostController.navigate(route = "detail") }
                    )

                is ApodViewModel.Result.Error ->
                    ErrorContent(
                        result.error
                    )
            }
        },
    )
}

@Composable
private fun ErrorContent(
    error: Exception
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "An error has occurred accessing the Apod archive:\n+ $error",
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ApodContent(
    bitmapLoader: IBitmapLoader,
    apod: Apod,
    goToDetail: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        if (apod.isImage()) {
            BitmapImageForUrl(
                url = apod.url,
                bitmapLoader = bitmapLoader,
            )
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
            onClick = { goToDetail() },
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
                contentDescription = "Go to detail screen",
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
            text = "Sorry, media type \"$mediaType\" is not yet supported.",
            textAlign = TextAlign.Center,
        )
    }
}