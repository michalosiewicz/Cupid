package com.mo.cupid.managers

import android.content.Intent
import android.net.Uri

class IntentManager {

    fun pickImage(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        return intent
    }

    fun openGallery(): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        return intent
    }

    fun getSelectedPhotos(photos: Intent): List<Uri> {
        val pickedImgUris = mutableListOf<Uri>()
        if (photos.clipData != null) {
            val count: Int = photos.clipData!!.itemCount

            for (i in 0 until count) {
                val imageURI: Uri = photos.clipData!!.getItemAt(i).uri

                pickedImgUris.add(imageURI)
            }
        } else {
            val imageUri: Uri? = photos.data

            if (imageUri != null) {
                pickedImgUris.add(imageUri)
            }
        }
        return pickedImgUris
    }
}