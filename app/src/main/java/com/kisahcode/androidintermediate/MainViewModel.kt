package com.kisahcode.androidintermediate

import androidx.lifecycle.ViewModel
import com.kisahcode.androidintermediate.data.UploadRepository
import java.io.File

/**
 * ViewModel responsible for handling business logic related to image upload operations.
 *
 * This ViewModel serves as a communication bridge between the UI layer and the data layer,
 * facilitating the upload of image files along with their descriptions to the server.
 *
 * @property repository The repository responsible for handling image upload operations.
 * @constructor Creates an instance of MainViewModel with the specified repository.
 */
class MainViewModel(private val repository: UploadRepository) : ViewModel() {
    /**
     * Initiates the process of uploading an image file along with its description to the server.
     * This function delegates the upload operation to the associated repository.
     *
     * @param file The image file to upload.
     * @param description The description associated with the image.
     * @return A LiveData object representing the current state of the upload operation.
     */
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}