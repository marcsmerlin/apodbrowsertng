package com.marcsmerlin.randomapod

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.marcsmerlin.randomapod.utils.BitmapImageForUrl
import com.marcsmerlin.randomapod.utils.IBitmapLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    appName: String,
    bitmapLoader: IBitmapLoader,
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
    bitmapLoader: IBitmapLoader,
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
            )
        },
    )
}

@Composable
private fun MyDrawerContent(
    scaffoldState: ScaffoldState,
    navHostController: NavHostController,
    coroutineScope: CoroutineScope,
) {
    @Composable
    fun MyTextButton(
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
        MyTextButton(text = "About", route = "about")
        MyTextButton(text = "Credits", route = "credits")
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
    bitmapLoader: IBitmapLoader,
    navHostController: NavHostController,
    result: State<ApodViewModel.Result>,

    ) {
    when (val value = result.value) {

        is ApodViewModel.Result.Data ->
            ApodContent(
                bitmapLoader = bitmapLoader,
                apod = value.apod,
                goToDetail = { navHostController.navigate(route = "detail") },
            )

        is ApodViewModel.Result.Error ->
            ErrorContent(
                error = value.error
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
            UnsupportedMediaType(mediaType = apod.mediaType)
        }

        val overlayBackground = MaterialTheme.colors.surface.copy(alpha = 0.50f)

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
private fun UnsupportedMediaType(
    mediaType: String
) {
    val text = """
        Sorry, the media type "$mediaType" is not yet supported.
        Click on the info button above for text details.
        """.trimIndent()

    TextNotice(text = text)
}

@Composable
private fun ErrorContent(
    error: Exception
) {
    val text = "An error has occurred accessing the Apod archive:\n+$error"

    TextNotice(text = text)
}


@Composable
private fun TextNotice(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
        )
    }
}