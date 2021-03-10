package com.marcsmerlin.randomapod.utils

import android.graphics.Bitmap
import androidx.compose.runtime.State

sealed class BitmapDownloadStatus(val url: String) {

    class Loading(url: String) : BitmapDownloadStatus(url)
    class Failed(url: String, val error: Exception) : BitmapDownloadStatus(url)
    class Done(url:String, val bitmap: Bitmap) : BitmapDownloadStatus(url)
}

interface IBitmapLoader {
    fun queueRequest(url: String): State<BitmapDownloadStatus>
}