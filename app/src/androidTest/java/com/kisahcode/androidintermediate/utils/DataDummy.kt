package com.kisahcode.androidintermediate.utils

import com.kisahcode.androidintermediate.data.local.entity.NewsEntity
import com.kisahcode.androidintermediate.data.remote.response.ArticlesItem
import com.kisahcode.androidintermediate.data.remote.response.NewsResponse
import com.kisahcode.androidintermediate.data.remote.response.Source

/**
 * A utility class for generating dummy data for testing purposes.
 */
object DataDummy {

    /**
     * Generates a list of dummy NewsEntity objects.
     */
   fun generateDummyNewsEntity(): List<NewsEntity> {

       val newsList = ArrayList<NewsEntity>()

       for (i in 0..10) {
           val news = NewsEntity(
               "Title $i",
               "2022-02-22T22:22:22Z",
               "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
               "https://www.dicoding.com/",
           )
           newsList.add(news)
       }

       return newsList
   }

    /**
     * Generates dummy news response data for testing purposes.
     * This function creates a list of dummy articles items and wraps them in a [NewsResponse] object.
     *
     * @return A [NewsResponse] object containing dummy news data.
     */
    fun generateDummyNewsResponse(): NewsResponse {
        // Initialize an empty list to hold dummy articles items.
        val newsList = ArrayList<ArticlesItem>()

        // Generate dummy articles items and add them to the list.
        for (i in 0..10) {
            val news = ArticlesItem(
                "2022-02-22T22:22:22Z",
                "author $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "description $i",
                Source("name", "id"),
                "Title $i",
                "https://www.dicoding.com/",
                "content $i",
            )
            newsList.add(news)
        }

        // Return a NewsResponse object containing the list of dummy articles items.
        return NewsResponse(newsList.size, newsList, "Success")
    }
}