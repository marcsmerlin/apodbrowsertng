package com.marcsmerlin.randomapod

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import com.marcsmerlin.randomapod.ui.theme.ApodBrowserTheme
import com.marcsmerlin.randomapod.utils.BitmapLoader

internal val LocalBitmapLoader = staticCompositionLocalOf<BitmapLoader> {
    error("No BitmapLoader supplied")
}

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ApodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer: AppContainer = (application as ApodApplication).container

        viewModel = ViewModelProvider(
            this,
            appContainer.viewModelFactory
        ).get(ApodViewModelImpl::class.java)

        setContent {
            ApodBrowserTheme {
                CompositionLocalProvider(
                    LocalBitmapLoader provides appContainer.bitmapLoader
                ) {
                    TopScreen(
                        appName = stringResource(id = R.string.app_name),
                        viewModel = viewModel,
                    )

                }
            }
        }
    }
}