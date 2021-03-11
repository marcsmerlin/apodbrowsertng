package com.marcsmerlin.randomapod.utils

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class BitmapLoaderImpl(
    private val requestQueue: BitmapRequestQueue
) : BitmapLoader {

    private var hasCache: Boolean = false

    private lateinit var cache: BitmapLoader.Status.Done

    override fun queueRequest(url: String): State<BitmapLoader.Status> {

        val result = mutableStateOf<BitmapLoader.Status>(
            BitmapLoader.Status.Loading(url = url)
        )

        if (hasCache && cache.url == url) {
            result.value = cache
        }
        else {
            val tag = this::class.java
            Log.i("$tag", "Queuing bitmap download request for: $url")

            requestQueue.addBitmapRequest(
                url = url,
                { bitmap ->
                    cache = BitmapLoader.Status.Done(
                            url = url,
                            bitmap = bitmap
                        )
                    hasCache = true

                    Log.i("$tag", "Bitmap downloaded for: $url")

                    result.value = cache
                },
                { exception ->
                    result.value =
                        BitmapLoader.Status.Failed(
                            url = url,
                            error = exception
                        )

                    Log.e("$tag", "Error encountered downloading bitmap for: $url")
                },
            )
        }

        return result
    }
}