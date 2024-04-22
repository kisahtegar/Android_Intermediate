package com.kisahcode.androidintermediate.data

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.kisahcode.androidintermediate.data.api.ApiService
import com.kisahcode.androidintermediate.data.api.FileUploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

/**
 * Repository class responsible for uploading images to the server.
 * It interacts with the ApiService to perform the upload operation.
 *
 * @param apiService The ApiService instance for making network requests.
 */
class UploadRepository private constructor(
    private val apiService: ApiService
) {

    /**
     * Uploads an image file along with a description to the server.
     *
     * This function returns a LiveData object that emits the current state of the upload operation.
     * The upload process includes converting the image file and description into request bodies,
     * creating a multipart request body, and sending it to the server using the ApiService.
     *
     * @param imageFile The image file to upload.
     * @param description The description associated with the image.
     * @return A LiveData object emitting the ResultState indicating the result of the upload operation.
     */
    fun uploadImage(imageFile: File, description: String) = liveData {
        // Emitting loading state while the upload operation is in progress.
        emit(ResultState.Loading)

        // Creating request bodies for the image file and description.
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

        // Creating a multipart request body with the image file.s
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            // Attempting to upload the image to the server.
            val successResponse = apiService.uploadImage(multipartBody, requestBody)

            // Emitting success state with the response data if the upload is successful.
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            // Handling errors that occur during the upload process.
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)

            // Emitting error state with the error message if the upload fails.
            emit(ResultState.Error(errorResponse.message))
        }

    }

    companion object {
        @Volatile
        private var instance: UploadRepository? = null


        /**
         * Returns the singleton instance of UploadRepository.
         *
         * @param apiService The ApiService instance.
         * @return The singleton instance of UploadRepository.
         */
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UploadRepository(apiService)
            }.also { instance = it }
    }
}