package com.example.imageEditor

import java.net.HttpURLConnection

import java.net.URL


fun getMethodHttp(endPoint: String, params: Map<String, Any?>): String {
    var conn: HttpURLConnection? = null
    try {
        val url = URL(BASE_URL + endPoint)
        conn = url.openConnection() as HttpURLConnection
        conn.setRequestProperty(
            "Content-Type",
            "application/json"
        ) // The format of the content we're sending to the server
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty("Authorization", "Client-ID K3hBadFpyF5VgNn4-n6dv116IEHWHtkfF-Mg5aRoryc")
    } catch (e: Exception) {
        throw e
    }
    return ""
}