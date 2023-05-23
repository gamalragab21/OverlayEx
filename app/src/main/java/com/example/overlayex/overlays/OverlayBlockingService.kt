package com.example.overlayex.overlays

import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.overlayex.MainActivity
import com.example.overlayex.R

class OverlayBlockingService : Service() {

    private lateinit var wakeLock: PowerManager.WakeLock
    private lateinit var keyguardLock: KeyguardManager.KeyguardLock

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // Acquire wake lock
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
            "OverlayBlockingService::WakeLock"
        )
        wakeLock.acquire()

        // Dismiss keyguard
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguardLock = keyguardManager.newKeyguardLock("OverlayBlockingService::KeyguardLock")
        keyguardLock.disableKeyguard()

        // Show a persistent notification to indicate that the service is running
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = createNotificationChannel("OverlayBlockingServiceChannel", "Overlay Blocking Service")
            startForeground(1, createNotification(channelId))
        } else {
            startForeground(1, createNotification(""))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release wake lock
        if (wakeLock.isHeld) {
            wakeLock.release()
        }

        // Re-enable keyguard
        keyguardLock.reenableKeyguard()
    }

    private fun createNotification(channelId: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Overlay Blocking Service")
            .setContentText("Running")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            return channelId
        }
        return ""
    }

    companion object {
        private const val CHANNEL_ID = "OverlayBlockingServiceChannel"
    }
}

