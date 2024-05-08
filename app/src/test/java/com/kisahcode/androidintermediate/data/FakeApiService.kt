package com.kisahcode.androidintermediate.data

import com.kisahcode.androidintermediate.data.remote.response.NewsResponse
import com.kisahcode.androidintermediate.data.remote.retrofit.ApiService
import com.kisahcode.androidintermediate.utils.DataDummy

/**
 * A fake implementation of [ApiService] used for unit testing the repository.
 *
 * This class provides dummy response data to simulate network API calls.
 */
class FakeApiService : ApiService {

    /**
     * Dummy news response generated from test data.
     */
    private val dummyResponse = DataDummy.generateDummyNewsResponse()

    /**
     * Simulates fetching news data from a remote API.
     * This method returns a dummy [NewsResponse] object.
     *
     * @param apiKey The API key used for authentication.
     * @return A [NewsResponse] object containing dummy news data.
     */
    override suspend fun getNews(apiKey: String): NewsResponse {
        return dummyResponse
    }
}