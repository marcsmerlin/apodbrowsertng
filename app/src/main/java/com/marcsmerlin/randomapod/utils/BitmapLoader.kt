package com.marcsmerlin.randomapod.utils

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class BitmapLoader(
    private val queue: IBitmapQueue
) : IBitmapLoader {

    override fun queueRequest(url: String): State<BitmapStatus> {

        val tag = this::class.java

        Log.i("$tag", "Bitmap request queued for: $url")

        val result = mutableStateOf<BitmapStatus>(
            BitmapStatus.Loading(url = url)
        )

        queue.addBitmapRequest(
            url = url,
            { bitmap ->
                result.value =
                    BitmapStatus.Success(
                        url = url,
                        bitmap = bitmap
                    )

                Log.i("$tag", "Bitmap received for: $url")
            },
            { exception ->
                result.value =
                    BitmapStatus.Error(
                        url = url,
                        error = exception
                    )
            },
        )

        return result
    }
}