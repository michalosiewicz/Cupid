package com.mo.cupid.managers

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants

class EditorManager {

    fun editPhoto(intent: Intent, uri: Uri): Intent {
        intent.data = uri
        intent.putExtra(
            DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
            "Images"
        )
        intent.putExtra(
            DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR,
            Color.parseColor("#006666")
        )
        intent.putExtra(
            DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,
            Color.parseColor("#FFFFFF")
        )
        return intent
    }
}