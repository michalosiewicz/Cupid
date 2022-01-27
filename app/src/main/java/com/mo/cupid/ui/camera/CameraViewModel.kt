package com.mo.cupid.ui.camera

import androidx.lifecycle.ViewModel
import com.mo.cupid.managers.CameraManager

class CameraViewModel(
    private val cameraManager: CameraManager
) : ViewModel() {

    fun startCamera() {
        cameraManager.startCamera()
    }

    fun changeCameraView() {
        cameraManager.changeView()
    }

    fun takePhoto() {
        cameraManager.takePhoto()
    }
}