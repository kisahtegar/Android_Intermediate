package com.kisahcode.androidintermediate.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kisahcode.androidintermediate.BuildConfig
import com.kisahcode.androidintermediate.data.local.entity.NewsEntity
import com.kisahcode.androidintermediate.data.local.room.NewsDao
import com.kisahcode.androidintermediate.data.remote.retrofit.ApiService

/**
 * Repository class responsible for handling data operations.
 *
 * This class acts as an intermediary between the data sources (remote and local) and the view model.
 * It provides methods for fetching, saving, and deleting news data.
 *
 * @property apiService The ApiService instance for making network requests.
 * @property newsDao The NewsDao instance for accessing local database operations.
 */
class NewsRepository(
    private val apiService: ApiService,
    private val newsDao: NewsDao
) {
    /**
     * Fetches headline news from the remote data source.
     *
     * This method retrieves headline news from the remote API using the ApiService instance.
     * It emits the loading state initially, then attempts to fetch the news data asynchronously.
     * If successful, it transforms the response into a list of NewsEntity objects and emits a success state with the data.
     * If an error occurs during the fetch operation, it emits an error state with the error message.
     *
     * @return A LiveData object representing the result of the operation.
     */
    fun getHeadlineNews(): LiveData<Result<List<NewsEntity>>> = liveData {
        // Emit the loading state
        emit(Result.Loading)
        try {
            // Make a network request to fetch news data from the remote API
            val response = apiService.getNews(BuildConfig.API_KEY)

            // Extract the articles from the response
            val articles = response.articles

            // Transform the articles into a list of NewsEntity objects
            val newsList = articles.map { article ->
                NewsEntity(
                    article.title,
                    article.publishedAt,
                    article.urlToImage,
                    article.url
                )
            }

            // Emit the success state with the list of news entities
            emit(Result.Success(newsList))
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")

            // Emit the error state with the error message
            emit(Result.Error(e.message.toString()))
        }
    }

    /**
     * Fetches bookmarked news from the local database.
     *
     * @return A LiveData object representing the list of bookmarked news.
     */
    fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
        return newsDao.getBookmarkedNews()
    }

    /**
     * Saves news to the local database.
     *
     * @param news The news entity to be saved.
     */
    suspend fun saveNews(news: NewsEntity) {
        newsDao.saveNews(news)
    }

    /**
     * Deletes news from the local database.
     *
     * @param title The title of the news to be deleted.
     */
    suspend fun deleteNews(title: String) {
        newsDao.deleteNews(title)
    }

    /**
     * Checks if news is bookmarked in the local database.
     *
     * @param title The title of the news to be checked.
     * @return A LiveData object representing the boolean result indicating whether the news is bookmarked.
     */
    fun isNewsBookmarked(title: String): LiveData<Boolean> {
        return newsDao.isNewsBookmarked(title)
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        /**
         * Gets the singleton instance of the NewsRepository.
         *
         * @param apiService The ApiService instance.
         * @param newsDao The NewsDao instance.
         * @return The singleton instance of NewsRepository.
         */
        fun getInstance(
            apiService: ApiService,
            newsDao: NewsDao
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, newsDao)
            }.also { instance = it }
    }
}