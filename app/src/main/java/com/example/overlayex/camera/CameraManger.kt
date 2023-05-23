package com.example.overlayex.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import androidx.core.app.ActivityCompat

class CameraManger(private val context: Context) {

    interface CameraManagerCallback {
        fun onCameraOpened(camera: CameraDevice)
        fun onCameraClosed(camera: CameraDevice)
        fun onDisconnected(camera: CameraDevice)
        fun onCameraError(camera: CameraDevice?, error: Int)
    }

    private lateinit var cameraStateCallback: CameraManagerCallback
    private var cameraDevice: CameraDevice? = null

    // Set the callback for camera state notifications
    fun setCameraStateCallback(callback: CameraManagerCallback) {
        cameraStateCallback = callback
    }

    // Open the camera
    fun openCamera(i: Int? = null) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId =
            cameraManager.cameraIdList[i ?: 0] // Change to the appropriate camera ID if needed

        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            // Open the camera using CameraManager.openCamera()
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    // Notify the callback that the camera is opened
                    cameraStateCallback.onCameraOpened(camera)
                }

                override fun onClosed(camera: CameraDevice) {
                    cameraDevice = null
                    // Notify the callback that the camera is closed
                    cameraStateCallback.onCameraClosed(camera)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    cameraDevice = null
                    // Notify the callback that the camera is disconnected
                    cameraStateCallback.onDisconnected(camera)
                }

                override fun onError(camera: CameraDevice, error: Int) {
//                    cameraDevice = null
                    // Notify the callback that an error occurred while accessing the camera
                    cameraStateCallback.onCameraError(camera, error)
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            // Notify the callback about the camera error
            cameraStateCallback.onCameraError(cameraDevice, e.reason)
        }
    }

    // Close the camera
    fun closeCamera() {
        cameraDevice?.close()
        cameraDevice = null
    }
}
