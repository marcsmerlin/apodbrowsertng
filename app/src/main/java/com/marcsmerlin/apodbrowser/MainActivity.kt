package com.marcsmerlin.apodbrowser

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.ViewModelProvider
import com.marcsmerlin.apodbrowser.ui.theme.ApodBrowserTheme
import com.marcsmerlin.apodbrowser.utils.BitmapLoader
import com.marcsmerlin.apodbrowser.utils.IBitmapLoader
import com.marcsmerlin.apodbrowser.utils.VolleyBitmapQueue

class MainActivity : AppCompatActivity() {
    private lateinit var viewModelFactory: ApodViewModelFactory
    private lateinit var viewModel: ApodViewModel
    private lateinit var bitmapLoader: IBitmapLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as ApodBrowserApplication).container

        viewModelFactory = ApodViewModelFactory(appContainer.apodRepository)
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            ).get(ApodViewModel::class.java)

        bitmapLoader =
            BitmapLoader(
                queue =
                VolleyBitmapQueue(
                    context = applicationContext
                )
            )

        setContent {
            ApodBrowserTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ApodBrowserScreen(
                        apodStatus = viewModel.status,
                        bitmapLoader = bitmapLoader,
                    )
                }
            }
        }
    }
}