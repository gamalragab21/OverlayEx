package com.example.overlayex.foreground

import android.app.Application

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ForegroundAppListener())
    }
}
