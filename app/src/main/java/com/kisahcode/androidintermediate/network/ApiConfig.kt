package com.kisahcode.androidintermediate.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Configuration class for setting up the Retrofit client and creating an instance of the ApiService.
 *
 * This class provides a static method to obtain an ApiService instance configured with the base URL,
 * Gson converter factory, and OkHttpClient with logging interceptor.
 */
class ApiConfig {
   companion object{

       /**
        * Retrieves an instance of the ApiService configured with the base URL, Gson converter factory,
        * and OkHttpClient with logging interceptor.
        *
        * @return An instance of the ApiService interface for making network requests.
        */
       fun getApiService(): ApiService {
           // Create a logging interceptor to log network requests and responses
           val loggingInterceptor =
               HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

           // Create an OkHttpClient with the logging interceptor
           val client = OkHttpClient.Builder()
               .addInterceptor(loggingInterceptor)
               .build()

           // Create a Retrofit instance with the base URL, Gson converter factory, and OkHttpClient
           val retrofit = Retrofit.Builder()
               .baseUrl("https://quote-api.dicoding.dev/")
               .addConverterFactory(GsonConverterFactory.create())
               .client(client)
               .build()

           // Create and return an instance of the ApiService interface using Retrofit
           return retrofit.create(ApiService::class.java)
       }
   }
}
