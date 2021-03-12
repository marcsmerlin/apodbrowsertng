package com.marcsmerlin.randomapod.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley

class VolleyBitmapRequestQueue(
    context: Context
) :
    BitmapRequestQueue {
    private val queue = Volley.newRequestQueue(context)
    private val tag = this::class.java

    override fun queueRequest(
        url: String,
        bitmapListener: (Bitmap) -> Unit,
        errorListener: (Exception) -> Unit,
        maxWidth: Int,
        maxHeight: Int,
    ) {
        val request = ImageRequest(
            url,
            bitmapListener,
            maxWidth,
            maxHeight,
            ImageView.ScaleType.CENTER_INSIDE,
            Bitmap.Config.HARDWARE,
            errorListener,
        )
        request.tag = tag
        queue.add(request)
    }

    override fun close() {
        queue.cancelAll(tag)
    }
}