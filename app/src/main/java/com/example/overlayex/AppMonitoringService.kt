package com.example.overlayex

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.util.Log

class AppMonitoringService : Service() {

    private val CHECK_INTERVAL: Long = 60 * 1000 // Check every 1 minute
    private val APPS_TO_MONITOR = arrayOf("com.example.app1", "com.example.app2")

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate() {
        super.onCreate()
        handler = Handler()
        runnable = Runnable {
            checkApps()
            handler.postDelayed(runnable, CHECK_INTERVAL)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("TAG", "onStartCommand: ")
        handler.post(runnable)
        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    private fun checkApps() {
        val packageName = this.packageName
        if (!isAppInstalled(packageName)) {
            Log.e("TAG", "Uninstall event for this application ")
            // App is not installed, perform necessary actions
            // For example, you can show a notification or trigger some other functionality
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
