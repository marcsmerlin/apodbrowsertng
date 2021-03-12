package com.marcsmerlin.randomapod.utils

import android.graphics.Bitmap

interface BitmapRequestQueue {
    fun queueRequest(
        url: String,
        bitmapListener: (Bitmap) -> Unit,
        errorListener: (Exception) -> Unit,
        maxWidth: Int = 0,
        maxHeight: Int = 0,
    )

    fun close()
}