package com.marcsmerlin.randomapod

import android.app.Application

class ApodApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}