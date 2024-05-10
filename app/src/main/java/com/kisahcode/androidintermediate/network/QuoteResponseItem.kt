package com.kisahcode.androidintermediate.network

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Represents a single quote item obtained from the network response.
 *
 * This class serves as an entity in the Room database, storing quote information.
 * Each instance corresponds to a single quote retrieved from the network.
 *
 * @property id The unique identifier of the quote.
 * @property author The author of the quote.
 * @property en The text of the quote in English.
 */
@Entity(tableName = "quote")
data class QuoteResponseItem(

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("en")
	val en: String? = null
)