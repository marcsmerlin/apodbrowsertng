package com.marcsmerlin.apod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(
    viewModel: ApodViewModel,
    goBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    when (val result = viewModel.result.value) {

        is ApodViewModel.Result.Data ->

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    ScaffoldTopBar(
                        goBack = { goBack() })
                },
                content = {
                    ScaffoldContent(result.apod)
                }
            )

        is ApodViewModel.Result.Error -> {

        }
    }
}

@Composable
private fun ScaffoldContent(
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
private fun ScaffoldTopBar(
    goBack: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },

        navigationIcon = {
            IconButton(
                onClick = { goBack() },
                enabled = true
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Go back"
                )
            }
        })
}