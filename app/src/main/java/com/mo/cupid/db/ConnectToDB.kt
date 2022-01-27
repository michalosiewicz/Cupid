package com.mo.cupid.db

import com.mo.cupid.constants.Constants.SERVER_IP
import com.vishnusivadas.advanced_httpurlconnection.PutData

class ConnectToDB {

    fun connectToDB(field: Array<String>, data: Array<String>, phpPath: String): String {
        val putData = PutData(
            "http://${SERVER_IP}/cupid-project/database/${phpPath}",
            "POST",
            field,
            data
        )
        if (putData.startPut()) {
            if (putData.onComplete()) {
                return putData.result
            }
        }
        return "-1"
    }
}