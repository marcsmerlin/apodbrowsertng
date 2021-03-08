package com.marcsmerlin.randomapod.utils

import android.graphics.Bitmap
import androidx.compose.runtime.State

sealed class BitmapStatus(val url: String) {

    class Loading(url: String) : BitmapStatus(url)
    class Success(url:String, val bitmap: Bitmap) : BitmapStatus(url)
    class Error(url: String, val error: Exception) : BitmapStatus(url)
}

interface IBitmapLoader {
    fun queueRequest(url: String): State<BitmapStatus>
}