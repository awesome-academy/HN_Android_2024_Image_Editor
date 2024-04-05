package com.example.imageEditor

import android.net.Uri
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


fun getMethodHttp(endPoint: String, params: Map<String, String?>): String {
    var conn: HttpURLConnection? = null
    try {
        val builtURI = Uri.parse(BASE_URL + endPoint).buildUpon()
        params.forEach { (key, value) ->
            builtURI.appendQueryParameter(key, value)
        }

        val url = URL(builtURI.build().toString())
        conn = url.openConnection() as HttpURLConnection
        conn.setRequestProperty(
            "Content-Type",
            "application/json"
        ) // The format of the content we're sending to the server
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty(
            "Authorization",
            "Client-ID K3hBadFpyF5VgNn4-n6dv116IEHWHtkfF-Mg5aRoryc"
        )
        conn.connect()

        return convertInputStreamToString(conn.inputStream)
    } catch (e: Exception) {
        throw e
    } finally {
        conn?.disconnect()
    }
}

fun convertInputStreamToString(inputStream: InputStream): String {
    // Đọc dữ liệu từ InputStream và chuyển đổi thành chuỗi JSON
    val stringBuilder = StringBuilder()
    BufferedReader(InputStreamReader(inputStream)).use { reader ->
        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = reader.readLine()
        }
    }
    return stringBuilder.toString()
}