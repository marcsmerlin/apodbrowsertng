package com.marcsmerlin.randomapod.utils

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class BitmapLoaderImpl(
    private val requestQueue: BitmapRequestQueue
) : BitmapLoader {

    private object cache {
        var url: String? = null
        lateinit var bitmap: Bitmap
    }

    override fun queueRequest(url: String): State<BitmapLoader.Status> {

        val result = mutableStateOf<BitmapLoader.Status>(
            BitmapLoader.Status.Loading(url = url)
        )

        if (cache.url != null && cache.url == url) {
            result.value =
                BitmapLoader.Status.Done(
                    url = url,
                    bitmap = cache.bitmap
                )
        } else {
            val tag = this::class.java
            Log.i("$tag", "Queuing bitmap request for: $url")

            requestQueue.addBitmapRequest(
                url = url,
                { bitmap ->
                    result.value =
                        BitmapLoader.Status.Done(
                            url = url,
                            bitmap = bitmap
                        )

                    Log.i("$tag", "Bitmap received for: $url")

                    cache.url = url
                    cache.bitmap = bitmap
                },
                { exception ->
                    result.value =
                        BitmapLoader.Status.Failed(
                            url = url,
                            error = exception
                        )
                },
            )
        }

        return result
    }
}