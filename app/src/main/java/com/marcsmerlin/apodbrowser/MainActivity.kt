package com.marcsmerlin.apodbrowser

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.marcsmerlin.apodbrowser.ui.theme.ApodBrowserTheme

class MainActivity : AppCompatActivity() {
    private lateinit var modelViewImpl: ApodModelViewImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as ApodBrowserApplication).container

        modelViewImpl = ViewModelProvider(
            this,
            appContainer.viewModelFactory
        ).get(ApodModelViewImpl::class.java)

        setContent {
            ApodBrowserTheme {
                ApodBrowserUI(
                    modelViewImpl = modelViewImpl,
                    bitmapLoader = appContainer.bitmapLoader,
                )
            }
        }
    }
}