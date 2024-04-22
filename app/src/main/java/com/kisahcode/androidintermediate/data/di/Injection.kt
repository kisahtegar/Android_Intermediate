package com.kisahcode.androidintermediate.data.di

import com.kisahcode.androidintermediate.data.UploadRepository
import com.kisahcode.androidintermediate.data.api.ApiConfig

object Injection {
    fun provideRepository(): UploadRepository {
        val apiService = ApiConfig.getApiService()
        return UploadRepository.getInstance(apiService)
    }
}