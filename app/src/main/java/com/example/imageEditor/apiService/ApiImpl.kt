package com.example.imageEditor.apiService

import com.example.imageEditor.base.OnListenProcess
import com.example.imageEditor.getMethodHttp
import com.example.imageEditor.model.CollectionModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


class ApiImpl(private val onListenProcess: OnListenProcess) : Api {
    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    override fun getCollections(page: Int): List<CollectionModel> {
        var result: List<CollectionModel> = emptyList()
        onListenProcess.onProgress()
        val future: Future<List<CollectionModel>> = executorService.submit(Callable {
            try {
                val query = mapOf(Pair("page", page.toString()))
                val listType: Type? = object : TypeToken<List<CollectionModel>>() {}.type
                return@Callable Gson().fromJson(getMethodHttp("collections", query), listType)
            } catch (e: Exception) {
                throw Exception("Error occurred in Callable", e)
            }
        })
        try {
            result = future.get()
            onListenProcess.onSuccess()
        } catch (e: Exception) {
            onListenProcess.onError()
        }
        finally {
            executorService.shutdown()
        }
        return result
    }
}