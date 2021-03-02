package com.marcsmerlin.apodbrowser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController

@Composable
fun DetailScreen(
    apod: Apod,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail Screen") })
        },
        content = {
            Column(
                modifier = Modifier.fillMaxHeight(),
            ) {
                with(apod) {
                    Text(text = title)
                    Text(text = date)
                    Text(text = copyrightInfo)
                    Text(
                        text = explanation,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    )
}