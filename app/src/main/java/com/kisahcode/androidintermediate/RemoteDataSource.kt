package com.kisahcode.androidintermediate

import android.content.Context

/**
 * A data source responsible for fetching product details from a remote location.
 *
 * @property context The context used to access application-specific resources.
 */
class RemoteDataSource(private val context: Context) {

    /**
     * Retrieves the details of a product from the remote data source.
     *
     * @return A [ProductModel] object representing the product details.
     */
    fun getDetailProduct(): ProductModel {
        return ProductModel(
            name = context.getString(R.string.name),
            store = context.getString(R.string.store),
            size = context.getString(R.string.size),
            color = context.getString(R.string.color),
            desc = context.getString(R.string.description),
            image = R.drawable.shoes,
            date = context.getString(R.string.date),
            rating = context.getString(R.string.rating),
            price = context.getString(R.string.price),
            countRating = context.getString(R.string.countRating)
        )
    }
}