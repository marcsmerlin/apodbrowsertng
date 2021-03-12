package com.marcsmerlin.randomapod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(
    topBar: @Composable () -> Unit,
) {
    val aboutTitle = stringResource(R.string.about_title)
    val aboutText = stringResource(R.string.about_text)
    val nasaApodWebsiteURL = "https://apod.nasa.gov/apod/astropix.html"

    Scaffold(
        topBar = topBar,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Text(
                    text = aboutTitle,
                    style = MaterialTheme.typography.h6,

                    )
                Spacer(modifier = Modifier.padding(top = 18.dp))
                Text(
                    text = aboutText,
                    style = MaterialTheme.typography.body1,
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))
                Text(text = nasaApodWebsiteURL)
            }
        }
    )
}