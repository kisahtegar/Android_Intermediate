package com.kisahcode.androidintermediate.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Represents a single news entity stored locally in the database.
 *
 * This class is used to define the structure of the `news` table in the local database.
 * Each instance of NewsEntity represents a single news item.
 *
 * @property title The title of the news.
 * @property publishedAt The date and time when the news was published.
 * @property urlToImage The URL to the image associated with the news.
 * @property url The URL to the full article.
 */
@Parcelize
@Entity(tableName = "news")
class NewsEntity(
    @field:ColumnInfo(name = "title")
    @field:PrimaryKey
    val title: String,

    @field:ColumnInfo(name = "publishedAt")
    val publishedAt: String,

    @field:ColumnInfo(name = "urlToImage")
    val urlToImage: String? = null,

    @field:ColumnInfo(name = "url")
    val url: String? = null
) : Parcelable