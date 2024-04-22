package com.kisahcode.androidintermediate.api

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response from a file upload API request.
 *
 * Contains information about whether the upload was successful and any accompanying message.
 *
 * @param error Indicates whether an error occurred during the upload process.
 * @param message Message accompanying the upload response, providing additional information.
 */
data class FileUploadResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)