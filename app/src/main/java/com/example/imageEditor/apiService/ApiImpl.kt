package com.example.imageEditor.apiService

import com.example.imageEditor.base.CustomFuture
import com.example.imageEditor.base.OnListenProcess
import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.model.PhotoSearchModel
import com.example.imageEditor.utils.COLLECTION_ENDPOINT
import com.example.imageEditor.utils.PAGE
import com.example.imageEditor.utils.PER_PAGE
import com.example.imageEditor.utils.PHOTO_SEARCH_ENDPOINT
import com.example.imageEditor.utils.QUERY_SEARCH
import com.example.imageEditor.utils.fromJsonToList
import com.example.imageEditor.utils.getMethodHttp
import com.google.gson.Gson
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ApiImpl(private val onListenProcess: OnListenProcess) : Api {
    override fun getCollections(
        page: Int,
        onResult: (List<CollectionModel>?) -> Unit,
    ) {
        val executorService: ExecutorService = Executors.newCachedThreadPool()
        val futureTask: CustomFuture<List<CollectionModel>> =
            CustomFuture(
                Callable {
                    try {
                        val query = mapOf(Pair(PAGE, page.toString()))
                        return@Callable Gson().fromJsonToList<CollectionModel>(
                            getMethodHttp(COLLECTION_ENDPOINT, query),
                        )
                    } catch (e: Exception) {
                        throw Throwable(e)
                    }
                },
                onListenProcess,
            )
        executorService.submit(futureTask)
        try {
            onResult.invoke(futureTask.get())
        } catch (e: Exception) {
            onListenProcess.onError(Throwable(e))
        } finally {
            executorService.shutdown()
        }
    }

    override fun searchPhotos(
        page: Int,
        query: String,
        perPage: Int?,
        onResult: (PhotoSearchModel?) -> Unit,
    ) {
        val executorService: ExecutorService = Executors.newCachedThreadPool()
        val futureTask: CustomFuture<PhotoSearchModel> =
            CustomFuture(
                Callable {
                    try {
                        val queryValue =
                            mapOf(
                                Pair(PAGE, page.toString()),
                                Pair(QUERY_SEARCH, query),
                                Pair(
                                    PER_PAGE,
                                    perPage.toString(),
                                ),
                            )
                        return@Callable Gson().fromJson(
                            getMethodHttp(PHOTO_SEARCH_ENDPOINT, queryValue),
                            PhotoSearchModel::class.java,
                        )
                    } catch (e: Exception) {
                        throw Throwable(e)
                    }
                },
                onListenProcess,
            )
        executorService.submit(futureTask)
        try {
            onResult.invoke(futureTask.get())
        } catch (e: Exception) {
            onListenProcess.onError(Throwable(e))
        } finally {
            executorService.shutdown()
        }
    }

    companion object {
        private var instance: ApiImpl? = null

        @JvmStatic
        fun getInstance(onListenProcess: OnListenProcess): ApiImpl = instance ?: ApiImpl(onListenProcess).also { instance = it }
    }
}
