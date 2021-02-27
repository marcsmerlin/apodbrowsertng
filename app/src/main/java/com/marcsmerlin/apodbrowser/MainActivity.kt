package com.marcsmerlin.apodbrowser

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var viewModelFactory: ApodViewModelFactory
    private lateinit var viewModel: ApodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as ApodBrowserApplication).container

        viewModelFactory = ApodViewModelFactory(appContainer.apodRepository)
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            ).get(ApodViewModel::class.java)

        setContent {
            ApodBrowserApp(
                apodViewModel = viewModel,
                appContainer = appContainer,
            )
        }
    }
}