package com.marcsmerlin.randomapod.utils

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class BitmapLoaderImpl(
    private val queue: IBitmapQueue
) : BitmapLoader {

    override fun queueRequest(url: String): State<BitmapLoader.Status> {

        val tag = this::class.java

        Log.i("$tag", "Bitmap request queued for: $url")

        val result = mutableStateOf<BitmapLoader.Status>(
            BitmapLoader.Status.Loading(url = url)
        )

        queue.addBitmapRequest(
            url = url,
            { bitmap ->
                result.value =
                    BitmapLoader.Status.Done(
                        url = url,
                        bitmap = bitmap
                    )

                Log.i("$tag", "Bitmap received for: $url")
            },
            { exception ->
                result.value =
                    BitmapLoader.Status.Failed(
                        url = url,
                        error = exception
                    )
            },
        )

        return result
    }
}