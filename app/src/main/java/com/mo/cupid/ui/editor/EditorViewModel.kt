package com.mo.cupid.ui.editor

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mo.cupid.managers.EditorManager
import com.mo.cupid.managers.IntentManager
import com.mo.cupid.managers.PathManager
import com.mo.cupid.managers.UploadPhotosManager

class EditorViewModel(
    private val userName: String,
    private val eventName: String
) : ViewModel() {

    private val editorManager = EditorManager()
    private val intentManager = IntentManager()

    private val uploadPhotosManager = UploadPhotosManager()
    private val pathManager = PathManager()

    private val _pickImageIntent = MutableLiveData<Intent>()
    val pickImageIntent: LiveData<Intent>
        get() = _pickImageIntent

    private val _saveImageIntent = MutableLiveData<Intent>()
    val saveImageIntent: LiveData<Intent>
        get() = _saveImageIntent

    private val _sendSuccess = MutableLiveData<String>()
    val sendSuccess: LiveData<String>
        get() = _sendSuccess

    private val _sendError = MutableLiveData<String>()
    val sendError: LiveData<String>
        get() = _sendError

    val uriImage = MutableLiveData<Uri>()

    fun pickImage() {
        _pickImageIntent.value = intentManager.pickImage()
    }

    fun startEditor(intent: Intent, uri: Uri) {
        _saveImageIntent.value = editorManager.editPhoto(intent, uri)
    }

    fun sendPhoto(context: Context) {
        uriImage.value?.let { image ->
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
                    "1" -> _sendSuccess.postValue("The photo has been sent")
                    "0" -> _sendError.postValue("Incorrect values")
                    "-1" -> _sendError.postValue("Error - connection")
                }
            }.start()
        }
    }
}