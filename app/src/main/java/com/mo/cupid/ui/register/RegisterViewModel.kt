package com.mo.cupid.ui.register

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mo.cupid.constants.Constants
import com.mo.cupid.constants.Constants.SERVER_IP
import com.vishnusivadas.advanced_httpurlconnection.PutData

class RegisterViewModel : ViewModel() {

    private val _registerInSuccess = MutableLiveData<String>()
    val registerInSuccess: LiveData<String>
        get() = _registerInSuccess

    private val _registerInError = MutableLiveData<String>()
    val registerInError: LiveData<String>
        get() = _registerInError

    val email = MutableLiveData("")
    val userName = MutableLiveData("")
    val password = MutableLiveData("")

    fun registerUser() {
        if (userName.value != "" && password.value != "" && email.value != "") {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val field = arrayOfNulls<String>(3)
                field[0] = "username"
                field[1] = "password"
                field[2] = "email"

                val data = arrayOfNulls<String>(3)
                data[0] = userName.value
                data[1] = password.value
                data[2] = email.value

                val putData = PutData(
                    "http://$SERVER_IP/cupid-project/database/signup.php",
                    "POST",
                    field,
                    data
                )

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        when (putData.result) {
                            "1" -> _registerInSuccess.value = "Rejestracja zakończona powodzeniem "
                            "0" -> _registerInError.value = "Rejestracja zakończona niepowodzeniem"
                            "-1" -> _registerInError.value = "Błąd - połączenie"
                            "-2" -> _registerInError.value = "Błąd - uzupełnij wszystkie pola"
                        }

                    }
                }
            }
        } else {
            _registerInError.value = "Błąd - uzupełnij wszystkie pola"
        }
    }
}