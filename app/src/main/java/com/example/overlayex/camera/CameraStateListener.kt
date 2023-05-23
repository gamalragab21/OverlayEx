package com.example.overlayex.camera

import android.content.Context
import android.hardware.camera2.CameraManager
import com.example.overlayex.toLog

class CameraStateListener(context: Context) {

    interface CameraStateCallback {
        fun onCameraAvailable()
        fun onCameraUnavailable()
    }

    private lateinit var cameraStateCallback: CameraStateCallback
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    var cameraAvailable: Boolean = false
        private set

    private val cameraAvailabilityCallback = object : CameraManager.AvailabilityCallback() {
        override fun onPhysicalCameraAvailable(cameraId: String, physicalCameraId: String) {
            super.onPhysicalCameraAvailable(cameraId, physicalCameraId)
            "onPhysicalCameraAvailable $cameraId".toLog()
        }

        override fun onCameraAvailable(cameraId: String) {
            "onCameraAvailable $cameraId".toLog()
            cameraAvailable = true
            cameraStateCallback.onCameraAvailable()
        }

        override fun onCameraUnavailable(cameraId: String) {
            "onCameraUnavailable $cameraId".toLog()
            cameraAvailable = false
            cameraStateCallback.onCameraUnavailable()
        }
    }

    fun setCameraStateCallback(callback: CameraStateCallback) {
        cameraStateCallback = callback
    }

    fun startListening() {
        cameraManager.registerAvailabilityCallback(cameraAvailabilityCallback, null)
    }

    fun stopListening() {
        cameraManager.unregisterAvailabilityCallback(cameraAvailabilityCallback)
    }
}
