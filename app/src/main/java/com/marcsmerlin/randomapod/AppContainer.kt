package com.marcsmerlin.randomapod

import android.content.Context
import com.marcsmerlin.randomapod.utils.BitmapLoaderImpl
import com.marcsmerlin.randomapod.utils.BitmapLoader
import com.marcsmerlin.randomapod.utils.VolleyBitmapQueue
import com.marcsmerlin.randomapod.utils.VolleyStringQueue

interface AppContainer {
    val viewModelFactory: ApodViewModelFactory
    val bitmapLoader: BitmapLoader
}

class AppContainerImpl(
    private val applicationContext: Context
) : AppContainer {

    override val viewModelFactory: ApodViewModelFactory

    init {
        val archive: ApodArchive by lazy {
            val context = applicationContext
            ApodArchive(
                queue = VolleyStringQueue(context),
                endpoint = context.getString(R.string.apod_api_endpoint),
                apiKey = context.getString(R.string.apod_api_key),
                firstDate = context.getString(R.string.apod_api_first_date),
            )
        }

        viewModelFactory = ApodViewModelFactory(archive)
    }

    override val bitmapLoader: BitmapLoader by lazy {
        BitmapLoaderImpl(
            queue = VolleyBitmapQueue(
                context = applicationContext
            )
        )
    }
}