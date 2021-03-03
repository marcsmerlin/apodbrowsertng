package com.marcsmerlin.apod

import android.content.Context
import com.marcsmerlin.apod.utils.BitmapLoader
import com.marcsmerlin.apod.utils.IBitmapLoader
import com.marcsmerlin.apod.utils.VolleyBitmapQueue
import com.marcsmerlin.apod.utils.VolleyStringQueue

interface AppContainer {
    val viewModelFactory: ApodViewModelFactory
    val bitmapLoader: IBitmapLoader
}

class AppContainerImpl(
    private val applicationContext: Context
) : AppContainer {

    override val viewModelFactory: ApodViewModelFactory

    init {
        val apodRepository: ApodRepository by lazy {
            ApodRepository(
                queue = VolleyStringQueue(context = applicationContext),
                endpoint = "https://api.nasa.gov/planetary/apod",
                apiKey = "Z3k4WvkWdkXOUg9VOdlNGv3cJeGauZ2omfJkGtNE",
                firstDate = "1995-06-16",
            )
        }

        viewModelFactory = ApodViewModelFactory(apodRepository)
    }

    override val bitmapLoader: IBitmapLoader by lazy {
        BitmapLoader(
            queue = VolleyBitmapQueue(
                context = applicationContext
            )
        )
    }
}