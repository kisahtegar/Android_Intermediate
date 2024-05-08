package com.kisahcode.androidintermediate.data.remote.response

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response from the news API.
 *
 * This class contains information about the total number of results,
 * the status of the response, and a list of articles retrieved from the API.
 *
 * @property totalResults The total number of results available.
 * @property articles A list of articles retrieved from the API.
 * @property status The status of the response (e.g., "ok" or "error").
 */
data class NewsResponse(

    @field:SerializedName("totalResults")
    val totalResults: Int,

    @field:SerializedName("articles")
    val articles: List<ArticlesItem>,

    @field:SerializedName("status")
    val status: String
)

/**
 * Data class representing the source of an article.
 *
 * @property name The name of the source.
 * @property id The unique identifier of the source.
 */
data class Source(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Any
)

/**
 * Data class representing an article retrieved from the news API.
 *
 * This class contains information such as the publication date, author, title,
 * description, URL to the article, and the source of the article.
 *
 * @property publishedAt The publication date of the article.
 * @property author The author of the article.
 * @property urlToImage The URL to the image associated with the article.
 * @property description A brief description of the article.
 * @property source The source of the article.
 * @property title The title of the article.
 * @property url The URL to the full article.
 * @property content The content of the article.
 */
data class ArticlesItem(

    @field:SerializedName("publishedAt")
    val publishedAt: String,

    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("urlToImage")
    val urlToImage: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("source")
    val source: Source,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("content")
    val content: Any
)
