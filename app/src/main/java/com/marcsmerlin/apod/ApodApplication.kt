package com.marcsmerlin.apod

import android.app.Application

class ApodApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}