package com.example.imageEditor.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJsonToList(json: String): List<T> {
    val listType = object : TypeToken<List<T>>() {}.type
    return this.fromJson(json, listType)
}
