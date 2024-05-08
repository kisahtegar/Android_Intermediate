package com.kisahcode.androidintermediate.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kisahcode.androidintermediate.data.local.entity.NewsEntity

/**
 * Data Access Object (DAO) interface for accessing news data from the local database.
 *
 * This interface defines methods to perform CRUD operations on the `news` table in the local database.
 */
@Dao
interface NewsDao {

    /**
     * Retrieves all bookmarked news items from the local database.
     *
     * @return A LiveData containing a list of all bookmarked news items.
     */
    @Query("SELECT * FROM news")
    fun getBookmarkedNews(): LiveData<List<NewsEntity>>

    /**
     * Saves a news item to the local database.
     *
     * @param news The news item to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNews(news: NewsEntity)

    /**
     * Deletes a news item from the local database based on its title.
     *
     * @param newsTitle The title of the news item to be deleted.
     */
    @Query("DELETE FROM news WHERE title = :newsTitle")
    suspend fun deleteNews(newsTitle: String)

    /**
     * Checks whether a news item is bookmarked in the local database.
     *
     * @param title The title of the news item to check.
     * @return A LiveData indicating whether the news item is bookmarked (true) or not (false).
     */
    @Query("SELECT EXISTS(SELECT * FROM news WHERE title = :title)")
    fun isNewsBookmarked(title: String): LiveData<Boolean>
}