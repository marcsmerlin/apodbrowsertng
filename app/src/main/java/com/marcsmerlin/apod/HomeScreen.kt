package com.marcsmerlin.apod

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcsmerlin.apod.utils.BitmapImageForUrl
import com.marcsmerlin.apod.utils.IBitmapLoader

@Composable
fun HomeScreen(
    bitmapLoader: IBitmapLoader,
    viewModel: ApodViewModel,
    goToDetail: () -> Unit,
) {
    when (val result = viewModel.result.value) {

        is ApodViewModel.Result.Data ->
            DataScreen(
                bitmapLoader,
                result.apod,
                isHome = viewModel::isHome,
                goHome = viewModel::goHome,
                getRandom = viewModel::getRandom,
                goToDetail = goToDetail
            )

        is ApodViewModel.Result.Error ->
            ErrorScreen(result.error)
    }
}

@Composable
private fun DataScreen(
    bitmapLoader: IBitmapLoader,
    apod: Apod,
    isHome: () -> Boolean,
    goHome: () -> Unit,
    getRandom: () -> Unit,
    goToDetail: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val enableHome = !remember(apod) { isHome() }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ScaffoldTopBar(
                enableHome = enableHome,
                goHome = goHome,
                getRandom = getRandom,
            )
        },
        content = {
            ScaffoldContent(
                bitmapLoader = bitmapLoader,
                apod = apod,
                goToDetail = goToDetail,
            )
        },
    )
}

@Composable
private fun ErrorScreen(error: Exception) {
    Log.e("Error Screen", error.toString())
}

@Composable
private fun ScaffoldTopBar(
    enableHome: Boolean,
    goHome: () -> Unit,
    getRandom: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },

        navigationIcon = {
            IconButton(
                onClick = { },
                enabled = false
            ) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = { getRandom() }) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = "Get random APOD"
                )
            }
            IconButton(
                onClick = { goHome() },
                enabled = enableHome
            ) {
                if (enableHome) {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Go to APOD home",
                        tint = Color.DarkGray,
                    )
                } else {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Go to APOD home"
                    )
                }
            }
        })
}

@Composable
private fun ScaffoldContent(
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
                contentDescription = "Go to detail",
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