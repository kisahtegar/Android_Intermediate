package com.kisahcode.androidintermediate.di

import android.content.Context
import com.kisahcode.androidintermediate.data.NewsRepository
import com.kisahcode.androidintermediate.data.local.room.NewsDatabase
import com.kisahcode.androidintermediate.data.remote.retrofit.ApiConfig

object Injection {

    /**
     * Provides a NewsRepository instance.
     *
     * This method creates and returns a NewsRepository instance by initializing its dependencies.
     * It obtains an instance of ApiService using ApiConfig, and initializes a NewsDatabase instance
     * using the provided application context. Finally, it retrieves the NewsDao from the database
     * and constructs a NewsRepository object with the ApiService and NewsDao instances.
     *
     * @param context The application context.
     * @return An instance of NewsRepository.
     */
    fun provideRepository(context: Context): NewsRepository {
        // Obtain an instance of ApiService using ApiConfig
        val apiService = ApiConfig.getApiService()

        // Initialize a NewsDatabase instance using the provided application context
        val database = NewsDatabase.getInstance(context)

        // Retrieve the NewsDao from the database
        val dao = database.newsDao()

        // Construct a NewsRepository object with the ApiService and NewsDao instances
        return NewsRepository.getInstance(apiService, dao)
    }
}