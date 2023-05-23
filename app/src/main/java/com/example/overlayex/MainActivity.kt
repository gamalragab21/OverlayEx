package com.example.overlayex

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraDevice
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.FLAG_WINDOW_IS_OBSCURED
import android.view.MotionEvent.FLAG_WINDOW_IS_PARTIALLY_OBSCURED
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.overlayex.camera.CameraManger
import com.example.overlayex.camera.CameraStateListener
import com.example.overlayex.databinding.ActivityMainBinding
import com.example.overlayex.overlays.OverLayHelper
import com.example.overlayex.overlays.OverlayService


class MainActivity : AppCompatActivity(), CameraStateListener.CameraStateCallback,
    CameraManger.CameraManagerCallback {

    private lateinit var binding: ActivityMainBinding
    private val foregroundServiceRequestCode = 100

    private val cameraHelper by lazy {
        CameraManger(this).apply {
            setCameraStateCallback(this@MainActivity)
        }
    }

    private val overLayHelper by lazy { OverLayHelper(this) }

    private val cameraStateListener by lazy {
        CameraStateListener(this).apply {
            setCameraStateCallback(this@MainActivity)
        }
    }

    class MyReaderCallback : NfcAdapter.ReaderCallback {
        override fun onTagDiscovered(tag: Tag?) {
            // Handle NFC tag discovery here
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serviceIntent = Intent(this, AppMonitoringService::class.java)
        startService(serviceIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setHideOverlayWindows(true)
        } else {
            "fff".toLog()

            val view: ViewGroup = findViewById(R.id.main_Veiw)
            view.filterTouchesWhenObscured = true
            view.setOnTouchListener { v, event ->
                val flags = event.flags
                val theBadTouch = flags and FLAG_WINDOW_IS_PARTIALLY_OBSCURED != 0
                return@setOnTouchListener theBadTouch
            }
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//
//        window.addFlags( FLAG_NOT_FOCUSABLE)
////        overLayHelper.preventOverLay()
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN )
        binding.startOverlayButton.setOnClickListener {
//            val apps=overLayHelper.getApps()
//            apps.toString().toLog()
//            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
//            val hh = overLayHelper.hasOverlayViews()
//            overLayHelper.isOverlayPermissionGranted().also {
//                if (it) overLayHelper.detectOverlays(binding.root)
//            }
//            overLayHelper.ff(window)

            // Start the service to block overlay features
// Request foreground service permission if running on Android 12 or later
//            if (Build        MainActivity.this.getWindow().setHideOverlayWindows(true);
//            if (cameraStateListener.cameraAvailable)
//                cameraHelper.openCamera()
//            else Toast.makeText(
//                this,
//                "Please Close Other Apps that using your camera , we can't complete ",
//                Toast.LENGTH_SHORT
//            ).show()
        }

        binding.openCamera2.setOnClickListener {
            Toast.makeText(this, "dkdkkd", Toast.LENGTH_SHORT).show()
            cameraHelper.openCamera(1)

        }

        binding.detectOverlayButton.setOnClickListener {
            cameraHelper.closeCamera()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return onFilterTouchEventForSecurity(event)
    }

    private fun onFilterTouchEventForSecurity(event: MotionEvent?): Boolean {
        // Add custom security check
        event?.let {
            val flags = event.flags
            val badTouch =
                flags and FLAG_WINDOW_IS_PARTIALLY_OBSCURED != 0 || flags and FLAG_WINDOW_IS_OBSCURED != 0
            return if (badTouch) {
                // consume touch event to block touch
                false
            } else onFilterTouchEventForSecurity(event)
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == foregroundServiceRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startOverlayBlockingService()
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    private fun startOverlayBlockingService() {
        // Start the service to block overlays
        // Start the overlay service
        stopService(Intent(this, OverlayService::class.java))
//        startService(Intent(this, OverlayBlockingService::class.java))
    }
//    override fun onResume() {
//        super.onResume()
//        cameraStateListener.startListening()
//    }

    override fun onStop() {
        super.onStop()
        cameraStateListener.stopListening()
    }

    override fun onCameraOpened(camera: CameraDevice) {
        "onCameraOpened".toLog()
    }

    override fun onCameraClosed(camera: CameraDevice) {
        "onCameraClosed".toLog()
    }

    override fun onCameraError(camera: CameraDevice?, error: Int) {
        "onCameraError $error".toLog()
        Toast.makeText(
            this,
            "onCameraError Unable open camera for reason: $error",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDisconnected(camera: CameraDevice) {
        "onDisconnected".toLog()
    }

    override fun onCameraAvailable() {
        binding.textView.text = "Camera State is Available"
        "onCameraAvailable".toLog()
    }

    override fun onCameraUnavailable() {
        binding.textView.text = "Camera State is Unavailable"
        "onCameraUnavailable".toLog()
    }

}

fun String.toLog() = Log.e("TAG", "toLog: $this")
