package com.example.imageEditor.repository

import com.example.imageEditor.apiService.DownloadService

class DetailRepository {
    private val downloadService = DownloadService.getInstance()

    fun downloadImage(url: String) {
        downloadService.downloadImage(url)
    }

    companion object {
        private var instance: DetailRepository? = null

        @JvmStatic
        fun getInstance(): DetailRepository {
            if (instance == null) {
                instance = DetailRepository()
            }
            return instance as DetailRepository
        }
    }
}
