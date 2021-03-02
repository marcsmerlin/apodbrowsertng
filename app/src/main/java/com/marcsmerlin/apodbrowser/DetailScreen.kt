package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(
    appTitle: String,
    apod: Apod,
    goBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    with(apod) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                ScaffoldTopBar(
                    title = appTitle,
                    goBack = { goBack() })
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(all = 24.dp),
                ) {
                    Text(text = title)
                    Text(text = date)
                    Text(text = explanation)
                    if (hasCopyrightInfo())
                        Text(text = "Credit: $copyrightInfo")
                }
            }
        )
    }
}

@Composable
private fun ScaffoldTopBar(
    title: String,
    goBack: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { goBack() }, enabled = true) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
            }
        })
}