package com.marcsmerlin.randomapod

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.marcsmerlin.randomapod.ui.theme.ApodBrowserTheme

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ApodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as ApodApplication).container

        viewModel = ViewModelProvider(
            this,
            appContainer.viewModelFactory
        ).get(ApodViewModelImpl::class.java)

        setContent {
            ApodBrowserTheme {
                ApodUI(
                    bitmapLoader = appContainer.bitmapLoader,
                    viewModel = viewModel,
                )
            }
        }
    }
}