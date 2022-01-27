package com.mo.cupid.ui.event.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mo.cupid.db.ConnectToDB

class JoinEventViewModel {

    private val connectToDB = ConnectToDB()

    private val _joinSuccess = MutableLiveData<String>()
    val joinSuccess: LiveData<String>
        get() = _joinSuccess

    private val _joinError = MutableLiveData<String>()
    val joinError: LiveData<String>
        get() = _joinError

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

    fun joinEvent() {
        val nameToDB = name.value ?: ""
        val passwordToDB = password.value ?: ""

        val result = connectToDB.connectToDB(
            arrayOf("name", "password"),
            arrayOf(nameToDB, passwordToDB),
            "joinEvent.php"
        )

        when (result) {
            "1" -> _joinSuccess.value = "Joined the event"
            "0" -> _joinError.value = "Incorrect name or password"
            "-1" -> _joinError.value = "Error - connection"
        }
    }
}