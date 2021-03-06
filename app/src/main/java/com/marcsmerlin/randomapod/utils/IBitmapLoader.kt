package com.marcsmerlin.randomapod.utils

import android.graphics.Bitmap
import androidx.compose.runtime.State

sealed class BitmapStatus {
    object Loading : BitmapStatus()
    data class Success(val bitmap: Bitmap) : BitmapStatus()
    data class Error(val error: Exception) : BitmapStatus()
}

interface IBitmapLoader {
    fun queueRequest(url: String): State<BitmapStatus>
}