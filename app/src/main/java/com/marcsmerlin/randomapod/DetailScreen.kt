package com.marcsmerlin.randomapod

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(
    viewModel: ApodViewModel,
    topBar: @Composable () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = topBar,
        content = {
            when (val result = viewModel.result.value) {

                is ApodViewModel.Result.Data ->
                    ApodDetail(result.apod)

                is ApodViewModel.Result.Error ->
                    ErrorDetail(result.error)

            }
        })
}

@Composable
private fun ApodDetail(
    apod: Apod,
) {
    with(apod) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(all = 24.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
            )
            Text(
                text = date,
                fontStyle = FontStyle.Italic,
            )
            if (hasCopyrightInfo())
                Text(
                    text = "Credit: $copyrightInfo",
                    fontStyle = FontStyle.Italic,
                )
            Spacer(modifier = Modifier.padding(top = 18.dp))
            Text(
                text = explanation,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ErrorDetail(error: Exception) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "Error detail:\n+ $error",
            textAlign = TextAlign.Center,
        )
    }
}