package com.example.overlayex.overlays

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.example.overlayex.toLog


class OverLayHelper constructor(private val activity: Activity) {

    fun getApps(): MutableList<String> {
        val packageManager = activity.packageManager
        val overlayApps = mutableListOf<String>()
        val activityManager = activity.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningApps = activityManager.runningAppProcesses

        for (processInfo in runningApps) {
            try {
                val packageName = processInfo.processName

                val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                val permissions = packageInfo.requestedPermissions

                if (permissions != null) {
                    for (permission in permissions) {
                        if (permission == Manifest.permission.SYSTEM_ALERT_WINDOW || permission == Manifest.permission.FOREGROUND_SERVICE) {
                            overlayApps.add(packageName)
                            break
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
        return overlayApps
    }
    @RequiresApi(Build.VERSION_CODES.R)
    fun hasOverlayViews(): Boolean {
        "hasOverlayViews".toLog()
        val activityManager = activity.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(Int.MAX_VALUE)

        for (taskInfo in runningTasks) {
            taskInfo.toString().toLog()
        }
        return false
    }


    fun isOverlayPermissionGranted(): Boolean = Settings.canDrawOverlays(activity)

    fun detectOverlays(view: View) {
        "detectOverlays".toLog()
        val windowManager = activity.getSystemService(WINDOW_SERVICE) as WindowManager
        val activityManager = activity.getSystemService(ACTIVITY_SERVICE) as ActivityManager

//        activityManager.runningAppProcesses.forEach {
//            if (it.processName != packageName) {
//                activityManager.forceStopPackage(it.processName)
//            }
//        }


    }

    fun preventOverLay() {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    fun bringTransactionToFront(view: View) {
        view.bringToFront()
    }

    fun scanViewHierarchy(view: View): Boolean {
        "scanViewHierarchy".toLog()

        if (isOverlayView(view)) {
            "Overlay detected".toLog()

            // Overlay detected
            // Handle the situation accordingly
            return true
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (scanViewHierarchy(child)) {
                    return true
                }
            }
        }
        return false
    }


    fun isOverlayView(view: View): Boolean {
        if (view.layoutParams is WindowManager.LayoutParams) {
            val layoutParams = view.layoutParams as WindowManager.LayoutParams

            // Check if the view is positioned outside the screen bounds
            if (layoutParams.x < 0 || layoutParams.y < 0) {
                return true
            }

            // Check if the view has a high z-index indicating it's on top of other views
            if (layoutParams.type >= WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) {
                return true
            }

            // Check if the view is fully transparent
            if (view.alpha == 0f) {
                return true
            }

            // Check if the view covers the entire screen
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels

            val viewRect = Rect()
            view.getGlobalVisibleRect(viewRect)
            if (viewRect.width() >= screenWidth && viewRect.height() >= screenHeight) {
                return true
            }

            // Add any additional checks based on your specific requirements and characteristics of overlays

            return false
        } else return false
    }

}