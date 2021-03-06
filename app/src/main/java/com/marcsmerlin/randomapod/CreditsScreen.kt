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

private val CreditsText =
    """
The Random Apod app was created by Marc Merlin as an exercise to learn the Kotlin programming language and the Android Development environment, in particular the newly introduced Jetpack Compose user interface framework. It is offered for use free of charge as a tribute to NASA's Astronomy Picture of the Day (Apod) website.

The source code for the app, intended to be suitable for educational purposes, is available under a GNU General Public License v3.0 at Marc's GitHub repository:
    """.trimIndent()

private val GitHubRepositoryURL = "https://github.com/marcsmerlin"

@Composable
fun CreditsScreen(
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
                    text = "Credits for Random Apod app",
                    style = MaterialTheme.typography.h6,

                    )
                Spacer(modifier = Modifier.padding(top = 18.dp))
                Text(
                    text = CreditsText,
                    style = MaterialTheme.typography.body1,
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))
                Text(text = GitHubRepositoryURL)
            }
        }
    )
}