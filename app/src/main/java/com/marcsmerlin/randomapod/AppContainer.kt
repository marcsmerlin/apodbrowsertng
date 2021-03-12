package com.marcsmerlin.randomapod

import android.content.Context
import com.marcsmerlin.randomapod.utils.BitmapLoaderImpl
import com.marcsmerlin.randomapod.utils.BitmapLoader
import com.marcsmerlin.randomapod.utils.VolleyBitmapRequestQueue
import com.marcsmerlin.randomapod.utils.VolleyStringRequestQueue

class AppContainer(
    private val applicationContext: Context
) {
    val viewModelFactory: ApodViewModelImpl.Factory

    init {
        val archive: ApodArchive by lazy {
            val context = applicationContext
            ApodArchive(
                requestQueue = VolleyStringRequestQueue(context),
                endpoint = context.getString(R.string.apod_api_endpoint),
                apiKey = context.getString(R.string.apod_api_key),
                firstDate = context.getString(R.string.apod_api_first_date),
            )
        }

        viewModelFactory = ApodViewModelImpl.Factory(archive)
    }

    val bitmapLoader: BitmapLoader by lazy {
        BitmapLoaderImpl(
            requestQueue = VolleyBitmapRequestQueue(
                context = applicationContext
            )
        )
    }
}