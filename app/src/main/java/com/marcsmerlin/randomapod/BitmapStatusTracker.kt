package com.marcsmerlin.randomapod

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.marcsmerlin.randomapod.utils.BitmapStatus

@Composable
fun BitmapStatusTracker(
    bitmapStatus: State<BitmapStatus>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    loading: @Composable (String) -> Unit = { _ -> },
    error: @Composable (String, Exception) -> Unit = { _, _ -> },
    success: @Composable (String, Bitmap) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
    ) {

        when (val value = bitmapStatus.value) {
            is BitmapStatus.Loading -> {
                loading(value.url)
            }

            is BitmapStatus.Error -> {
                error(value.url, value.error)
            }

            is BitmapStatus.Success -> {
                success(value.url, value.bitmap)
            }
        }
    }
}