package com.marcsmerlin.apodbrowser

import android.app.Application

class AppContainer(application: Application) {

    private val queue = VolleyStringQueue(context = application.applicationContext)

    private val repository =
        ApodRepository(
            queue = queue,
            endpoint = "https://api.nasa.gov/planetary/apod",
            apiKey = "Z3k4WvkWdkXOUg9VOdlNGv3cJeGauZ2omfJkGtNE",
            firstDate = "1995-06-16",
        )

    var viewModel = ApodViewModel(repository = repository)
}

class ApodBrowserApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}