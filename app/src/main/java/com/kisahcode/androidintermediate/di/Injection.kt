package com.kisahcode.androidintermediate.di

import android.content.Context
import com.kisahcode.androidintermediate.data.NewsRepository
import com.kisahcode.androidintermediate.data.local.room.NewsDatabase
import com.kisahcode.androidintermediate.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getInstance(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}