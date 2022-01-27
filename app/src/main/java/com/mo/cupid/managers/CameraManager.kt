package com.mo.cupid.managers

import android.app.Activity
import android.icu.text.SimpleDateFormat
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
import com.mo.cupid.providers.MessageProvider


class CameraManager(
    private val activity: Activity,
    private val fragment: Fragment
) {

    private val messageProvider = MessageProvider(activity)
    private var imageCapture: ImageCapture? = null
    private var outputDirectory: File

    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    init {
        outputDirectory = getOutputDirectory()
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(fragment.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    fragment, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    fun changeView() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA

        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "The photo has been saved"
                    MediaScannerConnection.scanFile(
                        activity, arrayOf(photoFile.toString()),
                        null, null
                    )
                    messageProvider.toastMessage(msg)
                }
            })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
            File(it, fragment.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) {
            mediaDir
        } else {
            activity.filesDir
        }
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}