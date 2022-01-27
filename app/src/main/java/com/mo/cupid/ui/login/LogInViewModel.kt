package com.mo.cupid.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mo.cupid.db.ConnectToDB

class LogInViewModel : ViewModel() {

    private val connectToDB = ConnectToDB()

    private val _logInSuccess = MutableLiveData<String>()
    val logInSuccess: LiveData<String>
        get() = _logInSuccess

    private val _logInError = MutableLiveData<String>()
    val logInError: LiveData<String>
        get() = _logInError

    val userName = MutableLiveData("")
    val password = MutableLiveData("")

    val areInputsCorrect = MediatorLiveData<Boolean>().apply {

        addSource(userName) {
            value = !userName.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        }
        addSource(password) {
            value = !userName.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        }
    }

    fun logIn() {
        val userNameToDB = userName.value ?: ""
        val passwordToDB = password.value ?: ""
        val result = connectToDB.connectToDB(
            arrayOf("username", "password"),
            arrayOf(userNameToDB, passwordToDB),
            "login.php"
        )
        when (result) {
            "1" -> _logInSuccess.value = "Success log in"
            "0" -> _logInError.value = "Incorrect login or password"
            "-1" -> _logInError.value = "Error - connection"
        }
    }
}