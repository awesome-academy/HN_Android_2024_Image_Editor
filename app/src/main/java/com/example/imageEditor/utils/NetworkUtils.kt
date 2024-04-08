package com.example.imageEditor.utils

import android.net.Uri
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun getMethodHttp(
    endPoint: String,
    params: Map<String, String?>,
): String {
    var conn: HttpURLConnection? = null
    try {
        val builtURI = Uri.parse(BASE_URL + endPoint).buildUpon()
        params.forEach { (key, value) ->
            builtURI.appendQueryParameter(key, value)
        }

        val url = URL(builtURI.build().toString())
        conn = url.openConnection() as HttpURLConnection
        conn.setRequestProperty(
            CONTENT_TYPE,
            "application/json",
        )
        conn.setRequestProperty(ACCEPT, "application/json")
        conn.setRequestProperty(
            AUTHORIZATION,
            ACCESS_TOKEN,
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
