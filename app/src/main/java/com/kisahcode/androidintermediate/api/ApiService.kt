package com.kisahcode.androidintermediate.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Retrofit interface for defining API endpoints related to file uploading.
 */
interface ApiService {

    /**
     * Uploads an image file along with its description to the server.
     *
     * @param file The image file to be uploaded as a multipart request part.
     * @param description The description of the image to be included in the request body.
     * @return A [FileUploadResponse] containing information about the upload operation.
     */
    @Multipart
    @POST("stories/guest")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): FileUploadResponse
}