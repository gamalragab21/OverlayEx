package com.example.overlayex.foreground

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.overlayex.toLog

class ForegroundAppListener : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
        // App resumed, another app is going to the background
        // Handle the event here
        " App resumed, another app is coming to the background".toLog()
    }

    override fun onActivityPaused(activity: Activity) {
        // App paused, another app is coming to the foreground
        // Handle the event here
        " App paused, another app is coming to the foreground".toLog()
    }

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}
