package com.mo.cupid.managers

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mo.cupid.R
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.util.*
import android.media.MediaScannerConnection


class CameraManager(
    private val activity: Activity,
    private val fragment: Fragment
) {

    private var imageCapture: ImageCapture? = null
    private var outputDirectory: File

    init {
        outputDirectory = getOutputDirectory()
    }

    fun startCamera(cameraSelector: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(fragment.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()


            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    fragment, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                //ERROR
            }

        }, ContextCompat.getMainExecutor(activity))
    }

    fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    //ERROR
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    MediaScannerConnection.scanFile(
                        activity, arrayOf(photoFile.toString()),
                        null, null
                    )
                    Toast.makeText(activity.baseContext, msg, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
            File(it, fragment.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) {
            Log.d("Test", mediaDir.toString())
            mediaDir
        } else {
            activity.filesDir
        }
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}