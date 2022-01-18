package com.mo.cupid.ui.login

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mo.cupid.constants.Constants.SERVER_IP
import com.vishnusivadas.advanced_httpurlconnection.PutData

class LogInViewModel : ViewModel() {

    private val _logInSuccess = MutableLiveData<String>()
    val logInSuccess: LiveData<String>
        get() = _logInSuccess

    private val _logInError = MutableLiveData<String>()
    val logInError: LiveData<String>
        get() = _logInError

    val userName = MutableLiveData("")
    val password = MutableLiveData("")

    fun logIn() {
        if (userName.value != "" && password.value != "") {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val field = arrayOfNulls<String>(2)
                field[0] = "username"
                field[1] = "password"

                val data = arrayOfNulls<String>(2)
                data[0] = userName.value
                data[1] = password.value

                val putData = PutData(
                    "http://$SERVER_IP/cupid-project/database/login.php",
                    "POST",
                    field,
                    data
                )

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        when (putData.result) {
                            "1" -> _logInSuccess.value = "Udało się poprawnie zalogować"
                            "0" -> _logInError.value = "Niepoprawny login lub hasło"
                            "-1" -> _logInError.value = "Błąd - połączenie"
                            "-2" -> _logInError.value = "Błąd - uzupełnij wszystkie pola"
                        }
                    }
                }
            }
        } else {
            _logInError.value = "Błąd - uzupełnij wszystkie pola"
        }
    }
}