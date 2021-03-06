package com.marcsmerlin.randomapod

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/*
DetailScreen: Composable used to display details of a result (data or error) returned by viewModel
request.
 */
@Composable
fun DetailScreen(
    viewModel: ApodViewModel,
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
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                })
        },
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