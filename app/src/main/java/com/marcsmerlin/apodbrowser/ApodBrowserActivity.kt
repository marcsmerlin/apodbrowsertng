package com.marcsmerlin.apodbrowser

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.marcsmerlin.apodbrowser.ui.theme.ApodBrowserTheme

class ApodBrowserActivity : AppCompatActivity() {
    private lateinit var viewModel: ApodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = (application as ApodBrowserApplication).container
        viewModel = container.viewModel

        setContent {
            ApodBrowserTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ApodBrowserScreen(
                        viewModel.result,
                        viewModel::getRandom,
                    )
                }
            }
        }
    }
}

@Composable
fun ApodBrowserScreen(
    result: State<ApodResult>,
    getRandom: () -> Unit,
) {

    when (val value = result.value) {
        is ApodError -> ApodErrorScreen(error = value)
        ApodLoading -> ApodLoadingScreen()
        is ApodSuccess ->
            ApodSuccessScreen(
                success = value,
                getRandom = getRandom
            )
    }
}

@Composable
private fun ApodLoadingScreen() {
    Text(text = "ApodBrowser is loading ...")
}

@Composable
private fun ApodErrorScreen(error: ApodError) {
    Text(text = "ApodBrowser has failed with error: ${error}.")
}

@Composable
private fun ApodSuccessScreen(
    success: ApodSuccess,
    getRandom: () -> Unit,
) {
    ApodDebugScreen(
        apod = success.apod,
        getRandom = getRandom
    )
}

@Composable
private fun ApodDebugScreen(
    apod: Apod,
    getRandom: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .clickable(onClick = getRandom)
    ) {
        with(apod) {
            Text(title)
            Text(date)
            Text(mediaType)
            Text(url)
            if (hasCopyrightInfo())
                Text(copyrightInfo)
            Text(explanation, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ApodBrowserTheme {
        ApodBrowserScreen(mutableStateOf(ApodLoading), getRandom = {})
    }
}