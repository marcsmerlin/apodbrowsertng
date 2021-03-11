package com.marcsmerlin.randomapod.utils

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class BitmapLoaderImpl(
    private val requestQueue: BitmapRequestQueue
) : BitmapLoader {

    private var hasCache: Boolean = false

    private lateinit var cache: MutableState<BitmapLoader.Status.Done>

    override fun queueRequest(url: String): State<BitmapLoader.Status> {

        val result = mutableStateOf<BitmapLoader.Status>(
            BitmapLoader.Status.Loading(url = url)
        )

        if (hasCache && cache.value.url == url) {
            result.value = cache.value
        }
        else {
            val tag = this::class.java
            Log.i("$tag", "Queuing bitmap request for: $url")

            requestQueue.addBitmapRequest(
                url = url,
                { bitmap ->
                    val value = BitmapLoader.Status.Done(
                            url = url,
                            bitmap = bitmap
                        )

                    Log.i("$tag", "Bitmap received for: $url")

                    result.value = value

                    if (hasCache) {
                        cache.value = value
                    }
                    else {
                        cache = mutableStateOf(value)
                        hasCache = true
                    }
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