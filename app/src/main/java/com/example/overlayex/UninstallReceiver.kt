package com.example.overlayex

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class UninstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        "Application is being uninstalled".toLog()
        if (intent.action == Intent.ACTION_PACKAGE_REMOVED) {
            val packageName = intent.data?.schemeSpecificPart
            if (packageName == context.packageName) {
                // Application is being uninstalled
                // Show your pop-up or initiate the deletion process here
                Toast.makeText(context, "Application is being uninstalled!", Toast.LENGTH_SHORT)
                    .show()
                Log.e("TAG", "onReceive: ${context.packageName}")
            }
        }
    }
}