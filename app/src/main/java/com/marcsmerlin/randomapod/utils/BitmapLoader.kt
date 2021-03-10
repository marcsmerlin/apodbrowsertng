package com.marcsmerlin.randomapod.utils

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class BitmapLoader(
    private val queue: IBitmapQueue
) : IBitmapLoader {

    override fun queueRequest(url: String): State<BitmapDownloadStatus> {

        val tag = this::class.java

        Log.i("$tag", "Bitmap request queued for: $url")

        val result = mutableStateOf<BitmapDownloadStatus>(
            BitmapDownloadStatus.Loading(url = url)
        )

        queue.addBitmapRequest(
            url = url,
            { bitmap ->
                result.value =
                    BitmapDownloadStatus.Done(
                        url = url,
                        bitmap = bitmap
                    )

                Log.i("$tag", "Bitmap received for: $url")
            },
            { exception ->
                result.value =
                    BitmapDownloadStatus.Failed(
                        url = url,
                        error = exception
                    )
            },
        )

        return result
    }
}