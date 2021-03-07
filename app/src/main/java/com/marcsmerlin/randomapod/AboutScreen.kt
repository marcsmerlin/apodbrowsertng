package com.marcsmerlin.randomapod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/*
AboutScreen: Composable used to present the About info for the Random Apod app.
 */

private val AboutText =
    """
Except for brief interruptions due to federal government shutdowns, NASA has been bringing a featured Astronomy Picture of the Day (Apod) to the public each and every day since June 16, 1995.

This Random Apod Android app uses a REST-accessible archive of these pictures provided courtesy of NASA. (Thank you, NASA!) Any deficiencies in their presentation here are solely the fault of the app's creator.

You can visit NASA's Astronomy Picture of the Day website to get a daily dose of fascinating astronomy imagery.
    """.trimIndent()

private const val NasaApodWebsiteURL = "https://apod.nasa.gov/apod/astropix.html"


@Composable
fun AboutScreen(
    appName: String,
    goBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = appName) },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                })
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Text(
                    text = "About NASA's APOD",
                    style = MaterialTheme.typography.h6,

                    )
                Spacer(modifier = Modifier.padding(top = 18.dp))
                Text(
                    text = AboutText,
                    style = MaterialTheme.typography.body1,
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))
                Text(text = NasaApodWebsiteURL)
            }
        }
    )
}