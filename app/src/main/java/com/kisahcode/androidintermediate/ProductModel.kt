package com.kisahcode.androidintermediate

/**
 * Represents a product in the application.
 *
 * @property name The name of the product.
 * @property price The price of the product as a string.
 * @property store The store where the product is available.
 * @property date The date when the product was added or updated.
 * @property rating The rating of the product as a string.
 * @property countRating The count of ratings for the product as a string.
 * @property size The size of the product.
 * @property color The color of the product.
 * @property desc A description of the product.
 * @property image The resource ID of the image associated with the product.
 */
data class ProductModel(
    val name: String,
    val price: String,
    val store: String,
    val date: String,
    val rating: String,
    val countRating: String,
    val size: String,
    val color: String,
    val desc: String,
    val image: Int
)