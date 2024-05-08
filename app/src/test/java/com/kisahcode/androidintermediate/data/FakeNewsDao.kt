package com.kisahcode.androidintermediate.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kisahcode.androidintermediate.data.local.entity.NewsEntity
import com.kisahcode.androidintermediate.data.local.room.NewsDao

/**
 * A fake implementation of the NewsDao interface for testing purposes.
 *
 * This class provides in-memory storage and LiveData functionality to simulate database operations.
 */
class FakeNewsDao : NewsDao {

    // In-memory list to store news entities.
    private var newsData = mutableListOf<NewsEntity>()

    /**
     * Retrieves LiveData containing a list of bookmarked news entities.
     *
     * @return LiveData containing a list of bookmarked news entities.
     */
    override fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
        // Create a MutableLiveData and set its value to the current list of news entities.
        val observableNews = MutableLiveData<List<NewsEntity>>()
        observableNews.value = newsData
        return observableNews
    }

    /**
     * Saves a news entity to the in-memory storage.
     *
     * @param news The news entity to be saved.
     */
    override suspend fun saveNews(news: NewsEntity) {
        newsData.add(news)
    }

    /**
     * Deletes a news entity from the in-memory storage based on its title.
     *
     * @param newsTitle The title of the news entity to be deleted.
     */
    override suspend fun deleteNews(newsTitle: String) {
        newsData.removeIf { it.title == newsTitle }
    }

    /**
     * Checks if a news entity with the given title exists in the in-memory storage.
     *
     * @param title The title of the news entity to check for existence.
     * @return LiveData indicating whether the news entity exists or not.
     */
    override fun isNewsBookmarked(title: String): LiveData<Boolean> {
        // Create a MutableLiveData and set its value based on the existence of the news entity in the list.
        val observableExistence = MutableLiveData<Boolean>()
        observableExistence.value = newsData.any { it.title == title }
        return observableExistence
    }
}