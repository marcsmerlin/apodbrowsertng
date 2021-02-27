package com.marcsmerlin.apodbrowser

import android.app.Application

class ApodBrowserApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}