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
import com.marcsmerlin.apodbrowser.utils.VolleyStringQueue

class MainActivity : AppCompatActivity() {
    private lateinit var viewModelFactory: ApodViewModelFactory
    private lateinit var viewModel: ApodViewModel
    private lateinit var bitmapLoader: IBitmapLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = ApodRepository(
            queue = VolleyStringQueue(context = applicationContext),
            endpoint = "https://api.nasa.gov/planetary/apod",
            apiKey = "Z3k4WvkWdkXOUg9VOdlNGv3cJeGauZ2omfJkGtNE",
            firstDate = "1995-06-16",
        )

        viewModelFactory = ApodViewModelFactory(repository)
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