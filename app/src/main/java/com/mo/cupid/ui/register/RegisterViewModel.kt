package com.mo.cupid.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mo.cupid.db.ConnectToDB

class RegisterViewModel : ViewModel() {

    private val connectToDB = ConnectToDB()

    private val _registerInSuccess = MutableLiveData<String>()
    val registerInSuccess: LiveData<String>
        get() = _registerInSuccess

    private val _registerInError = MutableLiveData<String>()
    val registerInError: LiveData<String>
        get() = _registerInError

    val email = MutableLiveData("")
    val userName = MutableLiveData("")
    val password = MutableLiveData("")

    val areInputsCorrect = MediatorLiveData<Boolean>().apply {

        addSource(email) {
            value =
                !email.value.isNullOrEmpty() && !userName.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        }

        addSource(userName) {
            value =
                !email.value.isNullOrEmpty() && !userName.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        }
        addSource(password) {
            value =
                !email.value.isNullOrEmpty() && !userName.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        }
    }

    fun registerUser() {
        val userNameToDB = userName.value ?: ""
        val passwordToDB = password.value ?: ""
        val emailToDB = email.value ?: ""

        val result = connectToDB.connectToDB(
            arrayOf("username", "password", "email"),
            arrayOf(userNameToDB, passwordToDB, emailToDB),
            "signup.php"
        )
        when (result) {
            "1" -> _registerInSuccess.value = "Success sign up"
            "0" -> _registerInError.value = "Registration failed"
            "-1" -> _registerInError.value = "Error - connection"
        }
    }
}