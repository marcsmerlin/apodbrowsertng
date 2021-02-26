package com.marcsmerlin.apodbrowser

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel

class AppContainer(applicationContext: Context) {

        val viewModel =
            ApodViewModel(
                repository =
                ApodRepository(
                    queue = VolleyStringQueue(context = applicationContext),
                    endpoint = "https://api.nasa.gov/planetary/apod",
                    apiKey = "Z3k4WvkWdkXOUg9VOdlNGv3cJeGauZ2omfJkGtNE",
                    firstDate = "1995-06-16",
                )
            )

        val bitmapLoader: IBitmapLoader =
            BitmapLoader(
                queue =
                VolleyBitmapQueue(
                    context = applicationContext
                )
            )
}

class ApodBrowserApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}