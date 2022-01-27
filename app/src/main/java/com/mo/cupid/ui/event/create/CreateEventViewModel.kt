package com.mo.cupid.ui.event.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mo.cupid.db.ConnectToDB

class CreateEventViewModel : ViewModel() {

    private val connectToDB = ConnectToDB()

    private val _createSuccess = MutableLiveData<String>()
    val createSuccess: LiveData<String>
        get() = _createSuccess

    private val _createError = MutableLiveData<String>()
    val createError: LiveData<String>
        get() = _createError

    val name = MutableLiveData("")
    val password = MutableLiveData("")

    val areInputsCorrect = MediatorLiveData<Boolean>().apply {

        addSource(name) {
            value = !name.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        }
        addSource(password) {
            value = !name.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        }
    }

    fun createEvent() {
        val nameToDB = name.value ?: ""
        val passwordToDB = password.value ?: ""

        val result = connectToDB.connectToDB(
            arrayOf("name", "password"),
            arrayOf(nameToDB, passwordToDB),
            "newEvent.php"
        )

        when (result) {
            "1" -> _createSuccess.value = "An event has been created"
            "0" -> _createError.value = "The event could not be created"
            "-1" -> _createError.value = "Error - connection"
        }
    }
}