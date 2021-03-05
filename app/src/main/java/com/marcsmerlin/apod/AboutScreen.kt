package com.marcsmerlin.apod

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

private val AboutText =
    """
Except for minor interruptions due to federal government shutdown, under award No. 80NSSC17M0076, NASA has been bringing a featured Astronomy Picture of the Day (Apod) to the public each and every day since June 16, 1995. With much gratitude, this Random Apod Android app accesses an archive of these pictures provided courtesy of NASA. Any deficiencies in their presentation or representation here are solely the fault of the app's creator, Marc Merlin.

Please visit NASA's Astronomy Picture of the Day website to get your daily astronomy dose: https://apod.nasa.gov/apod/astropix.html       
    """.trimIndent()

@Composable
fun AboutScreen(
    goBack: () -> Unit,
) {
    val appName = stringResource(id = R.string.app_name)
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
                    text = AboutText,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    )
}