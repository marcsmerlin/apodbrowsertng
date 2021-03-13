package com.marcsmerlin.randomapod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    appName: String,
    viewModel: ApodViewModel,
    navHostController: NavHostController,
) {
    MyScaffold(
        appName = appName,
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
                navHostController = navHostController,
                result = result,
                onRetryRequest = goToday,
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
                contentDescription = "Fetch random APOD from archive",
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
    navHostController: NavHostController,
    result: State<ApodViewModel.Result>,
    onRetryRequest: () -> Unit,

    ) {

    when (val value = result.value) {

        is ApodViewModel.Result.Data -> {
            ApodContent(
                apod = value.apod,
                goToDetail = { navHostController.navigate(route = "detail") },
                retry = onRetryRequest
            )
        }

        is ApodViewModel.Result.Error -> {
            val alertCause = "Error accessing Apod archive"

            RetryOrQuitAlert(
                error = value.error,
                alertCause = alertCause,
                onRetryRequest = onRetryRequest,
            )
        }
    }
}
