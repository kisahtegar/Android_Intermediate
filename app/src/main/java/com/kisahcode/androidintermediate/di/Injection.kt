package com.kisahcode.androidintermediate.di

import android.content.Context
import com.kisahcode.androidintermediate.data.QuoteRepository
import com.kisahcode.androidintermediate.database.QuoteDatabase
import com.kisahcode.androidintermediate.network.ApiConfig

/**
 * Object responsible for providing dependencies such as the QuoteRepository.
 */
object Injection {

    /**
     * Provides an instance of the QuoteRepository.
     *
     * @param context The application context.
     * @return QuoteRepository instance.
     */
    fun provideRepository(context: Context): QuoteRepository {
        // Initialize the database and API service instances
        val database = QuoteDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()

        // Create and return the QuoteRepository instance with the database and API service
        return QuoteRepository(database, apiService)
    }
}