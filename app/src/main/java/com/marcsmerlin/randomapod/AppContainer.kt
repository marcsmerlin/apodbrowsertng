package com.marcsmerlin.randomapod

import android.content.Context
import com.marcsmerlin.randomapod.utils.BitmapLoaderImpl
import com.marcsmerlin.randomapod.utils.BitmapLoader
import com.marcsmerlin.randomapod.utils.VolleyBitmapQueue
import com.marcsmerlin.randomapod.utils.VolleyStringQueue

class AppContainer(
    private val applicationContext: Context
) {

    val viewModelFactory: ApodViewModelImpl.Factory

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

        viewModelFactory = ApodViewModelImpl.Factory(archive)
    }

    val bitmapLoader: BitmapLoader by lazy {
        BitmapLoaderImpl(
            queue = VolleyBitmapQueue(
                context = applicationContext
            )
        )
    }
}