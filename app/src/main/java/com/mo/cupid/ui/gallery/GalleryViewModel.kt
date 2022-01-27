package com.mo.cupid.ui.gallery

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mo.cupid.managers.IntentManager
import com.mo.cupid.managers.PathManager
import com.mo.cupid.managers.UploadPhotosManager
import com.mo.cupid.ui.adapters.PhotosAdapter

class GalleryViewModel(
    private val userName: String,
    private val eventName: String
) : ViewModel() {

    private val uploadPhotosManager = UploadPhotosManager()
    private val pathManager = PathManager()
    private val intentManager = IntentManager()

    private val _intent = MutableLiveData<Intent>()
    val intent: LiveData<Intent>
        get() = _intent

    private val _sendSuccess = MutableLiveData<String>()
    val sendSuccess: LiveData<String>
        get() = _sendSuccess

    private val _sendError = MutableLiveData<String>()
    val sendError: LiveData<String>
        get() = _sendError

    private var pickedImgUris = listOf<Uri>()

    val photosAdapter = PhotosAdapter()
    val isInput = MutableLiveData(false)

    fun getSelectedPhotos(photos: Intent) {
        pickedImgUris = intentManager.getSelectedPhotos(photos)
        if (pickedImgUris.isNotEmpty()) {
            isInput.value = true
            photosAdapter.addNewItems(pickedImgUris)
        } else {
            isInput.value = false
        }
    }

    fun openGallery() {
        _intent.value = intentManager.openGallery()
    }

    fun sendPhotos(context: Context) {
        for (element in pickedImgUris) {
            element.let { image ->
                val path = pathManager.getPath(context, image)
                Thread {
                    val result = path?.let {
                        uploadPhotosManager.uploadFile(
                            it,
                            userName,
                            eventName
                        )
                    }
                    when (result) {
                        "1" -> _sendSuccess.postValue("The photos has been sent")
                        "0" -> _sendError.postValue("Incorrect values")
                        "-1" -> _sendError.postValue("Error - connection")
                    }
                }.start()
            }
        }
    }
}