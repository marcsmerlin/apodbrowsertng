package com.marcsmerlin.randomapod.utils

import android.graphics.Bitmap
import androidx.compose.runtime.State


interface BitmapLoader {

    sealed class Status(val url: String) {

        class Loading(url: String) : Status(url)
        class Failed(url: String, val error: Exception) : Status(url)
        class Done(url:String, val bitmap: Bitmap) : Status(url)
    }

    fun queueRequest(url: String): State<Status>
}