package com.marcsmerlin.apodbrowser

import android.content.Context
import com.marcsmerlin.apodbrowser.utils.VolleyStringQueue

interface AppContainer {
    val apodRepository: ApodRepository
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {

    override val apodRepository: ApodRepository by lazy {
        ApodRepository(
            queue = VolleyStringQueue(context = applicationContext),
            endpoint = "https://api.nasa.gov/planetary/apod",
            apiKey = "Z3k4WvkWdkXOUg9VOdlNGv3cJeGauZ2omfJkGtNE",
            firstDate = "1995-06-16",
        )
    }
}