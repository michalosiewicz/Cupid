package com.mo.cupid.providers

import android.content.Context
import android.widget.Toast


class MessageProvider(
    private val context: Context
) {

    fun toastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}