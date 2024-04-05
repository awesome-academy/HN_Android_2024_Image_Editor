package com.example.imageEditor.apiService

import android.os.Handler
import android.os.Looper
import com.example.imageEditor.base.OnListenProcess
import com.example.imageEditor.model.CollectionModel
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ApiImpl(private val onListenProcess: OnListenProcess) : Api {
    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    override fun getCollections(page: Int): List<CollectionModel> {
        executorService.execute {
            onListenProcess.onProgress()
            try {

            }
            catch (e:Exception){
                onListenProcess.onError()
            }
            finally {
                onListenProcess.onSuccess()
            }

        }
        return emptyList()
    }
}