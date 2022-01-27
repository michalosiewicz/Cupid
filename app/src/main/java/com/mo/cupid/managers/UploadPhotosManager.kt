package com.mo.cupid.managers

import android.webkit.MimeTypeMap
import com.mo.cupid.constants.Constants.SERVER_IP
import com.mo.cupid.db.ConnectToDB
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.Exception
import java.util.*

class UploadPhotosManager() {

    private val connectToDB = ConnectToDB()
    private val client = OkHttpClient()

    fun uploadFile(filePath: String, userName: String, eventName: String): String {

        val sourceFile = File(filePath)
        val sourceFileName = sourceFile.name

        val mime = getMimeType(sourceFile)

        val fileName: String = sourceFileName ?: sourceFile.name
        try {
            val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                    "uploaded_file",
                    fileName,
                    sourceFile.asRequestBody(mime?.toMediaTypeOrNull())
                )
                .build()

            val request: Request = Request.Builder().url(SERVER_URL).post(requestBody).build()
            val response: Response = client.newCall(request).execute()

            return if (response.isSuccessful) {
                addPhotoToDatabase(fileName, userName, eventName)
            } else {
                "-1"
            }
        } catch (e: Exception) {
            return "-1"
        }
    }

    private fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)

        if (extension != null) {
            type = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(extension.lowercase(Locale.getDefault()))
        }
        return type
    }

    private fun addPhotoToDatabase(fileName: String, userName: String, eventName: String): String {
        val resultPhoto = insertPhoto(fileName)
        return if (resultPhoto == "1") insertUserPhoto(
            getUserId(userName),
            getPhotoId(fileName),
            getEventId(eventName)
        ) else {
            resultPhoto
        }
    }

    private fun insertPhoto(fileName: String) =
        connectToDB.connectToDB(
            arrayOf("name"),
            arrayOf(fileName),
            "insert.php"
        )

    private fun getPhotoId(fileName: String) =
        connectToDB.connectToDB(
            arrayOf("name"),
            arrayOf(fileName),
            "getPhotoID.php"
        )

    private fun getUserId(userName: String) =
        connectToDB.connectToDB(
            arrayOf("name"),
            arrayOf(userName),
            "getUserID.php"
        )

    private fun getEventId(eventName: String) =
        connectToDB.connectToDB(
            arrayOf("name"),
            arrayOf(eventName),
            "getEventID.php"
        )

    private fun insertUserPhoto(userId: String, photoId: String, eventId: String) =
        connectToDB.connectToDB(
            arrayOf("id_u", "id_p", "id_e"),
            arrayOf(userId, photoId, eventId),
            "insertUserPhoto.php"
        )

    companion object {
        private const val SERVER_URL = "http://$SERVER_IP/cupid-project/upload.php"
    }
}