package com.marcsmerlin.apod.utils

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class BitmapLoader(
    private val queue: IBitmapQueue
) : IBitmapLoader {

    override fun queueRequest(url: String): State<BitmapStatus> {

        val tag = this::class.java

        Log.i("$tag", "Queuing request for: $url")

        val result = mutableStateOf<BitmapStatus>(BitmapStatus.Loading)

        queue.addBitmapRequest(
            url = url,
            { bitmap ->
                result.value = BitmapStatus.Success(bitmap)
                Log.i("$tag", "Bitmap received for: $url")
            },
            { exception -> result.value = BitmapStatus.Error(exception) },
        )

        return result
    }
}