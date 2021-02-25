package com.marcsmerlin.apodbrowser

import android.graphics.Bitmap

interface IBitmapQueue {
    fun addBitmapRequest(
        url: String,
        bitmapListener: (Bitmap) -> Unit,
        errorListener: (Exception) -> Unit,
        maxWidth: Int = 0,
        maxHeight: Int = 0,
    )

    fun close()
}