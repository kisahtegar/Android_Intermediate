package com.kisahcode.androidintermediate.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for defining API endpoints and request methods.
 *
 * This interface declares methods for making network requests to retrieve quotes from the API.
 */
interface ApiService {

    /**
     * Retrieves a list of quotes from the API.
     *
     * @param page The page number of quotes to retrieve.
     * @param size The number of quotes per page.
     * @return A list of QuoteResponseItem objects representing quotes obtained from the API.
     */
    @GET("list")
    suspend fun getQuote(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<QuoteResponseItem>
}