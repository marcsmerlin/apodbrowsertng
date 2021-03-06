package com.marcsmerlin.randomapod

import android.app.Application

/*
ApodApplication: Android Application subclass used to provide an AppContainer for accessing
application-scope resources.
 */
class ApodApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}