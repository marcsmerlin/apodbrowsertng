package com.marcsmerlin.apodbrowser

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.marcsmerlin.apodbrowser.ui.theme.ApodBrowserTheme

class ApodBrowserActivity : AppCompatActivity() {
    private lateinit var viewModel: ApodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = (application as ApodBrowserApplication).container
        viewModel = container.viewModel

        val bitmapLoader =
            BitmapLoader(
                VolleyBitmapQueue(context = applicationContext)
            )

        setContent {
            ApodBrowserTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ApodBrowserScreen(
                        apodResult = viewModel.result,
                        bitmapLoader = bitmapLoader,
                    )
                }
            }
        }
    }
}