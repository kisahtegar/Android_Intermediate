package com.kisahcode.androidintermediate.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object for configuring and obtaining an instance of the [ApiService].
 */
object ApiConfig {

    /**
     * Creates and configures an instance of [ApiService] with the base URL and necessary interceptors.
     *
     * @return An instance of [ApiService] configured to communicate with the API.
     */
    fun getApiService(): ApiService {
        // Create a logging interceptor for logging HTTP requests and responses.
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        // Create an OkHttpClient with the logging interceptor added.
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Create a Retrofit instance with GsonConverterFactory for JSON parsing.
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/") // Base URL of the API
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter for JSON parsing
            .client(client) // Set the OkHttpClient for network requests
            .build()

        // Create an instance of ApiService interface implementation.
        return retrofit.create(ApiService::class.java)
    }
}