package com.kisahcode.androidintermediate.data.remote.retrofit

import com.kisahcode.androidintermediate.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface defining the API endpoints for fetching news data.
 *
 * This interface declares a method for retrieving news articles based on specific parameters.
 */
interface ApiService {

    /**
     * Retrieves news articles from the API.
     *
     * @param apiKey The API key required for accessing the news data.
     * @return A [NewsResponse] object containing the list of news articles.
     */
    @GET("top-headlines?country=id&category=science")
    suspend fun getNews(@Query("apiKey") apiKey: String): NewsResponse
}