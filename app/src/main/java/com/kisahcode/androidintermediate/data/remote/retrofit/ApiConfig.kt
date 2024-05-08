package com.kisahcode.androidintermediate.data.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Object responsible for configuring the Retrofit instance and providing the ApiService.
 *
 * This object sets up the Retrofit instance with the base URL and necessary converters.
 * It also configures an OkHttpClient with a logging interceptor for debugging purposes.
 */
object ApiConfig {

    var BASE_URL = "https://newsapi.org/v2/"

    /**
     * Provides an instance of the ApiService for making API requests.
     *
     * @return An instance of the [ApiService].
     */
    fun getApiService(): ApiService {
        // Create a logging interceptor for logging network requests and responses
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        // Create an OkHttpClient with the logging interceptor
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Create a Retrofit instance with the base URL and Gson converter
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // Return the ApiService instance created by Retrofit
        return retrofit.create(ApiService::class.java)
    }
}