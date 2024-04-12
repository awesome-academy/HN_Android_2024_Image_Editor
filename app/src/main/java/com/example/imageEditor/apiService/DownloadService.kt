package com.example.imageEditor.apiService

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.imageEditor.App
import com.example.imageEditor.utils.FILE_TITLE
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DownloadService() {
    fun downloadImage(url: String) {
        val executorService: ExecutorService = Executors.newCachedThreadPool()
        executorService.submit {
            val request = DownloadManager.Request(Uri.parse(url))
            request.setTitle(FILE_TITLE)
            val fileName = "image/${UUID.randomUUID()}.jpg"
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            val downloadManager =
                App.instance.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }
    }

    companion object {
        private var instance: DownloadService? = null

        fun getInstance(): DownloadService {
            if (instance == null) {
                instance = DownloadService()
            }
            return instance as DownloadService
        }
    }
}
