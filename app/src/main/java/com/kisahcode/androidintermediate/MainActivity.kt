package com.kisahcode.androidintermediate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.kisahcode.androidintermediate.CameraActivity.Companion.CAMERAX_RESULT
import com.kisahcode.androidintermediate.data.ResultState
import com.kisahcode.androidintermediate.data.api.ApiConfig
import com.kisahcode.androidintermediate.data.api.FileUploadResponse
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException


/**
 * MainActivity of the Intent Gallery demonstration application.
 *
 * This activity demonstrates the usage of intent to pick visual media from the device gallery.
 * It allows users to select an image from the gallery and display it in an ImageView.
 * Currently, camera functionalities are implemented, and uploading the selected image is a placeholder.
 */
class MainActivity : AppCompatActivity() {

    // View binding for the activity layout.
    private lateinit var binding: ActivityMainBinding

    // URI of the currently selected image.
    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance()
    }

    /**
     * Activity result launcher for permission requests.
     *
     * It handles the result of the permission request and shows appropriate toast messages.
     */
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    /**
     * Checks if all required permissions are granted.
     *
     * @return True if all permissions are granted, false otherwise.
     */
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Checks if all required permissions are granted.
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        // Set click listeners for the buttons.
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    /**
     * Starts the gallery activity to pick an image.
     *
     * This method launches the gallery picker activity using the activity result launcher.
     * Upon selection of an image, the result is handled by the activity result callback.
     */
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    // Activity result launcher for gallery pick.
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // Set the URI of the selected image and display it.
            currentImageUri = uri
            showImage()
        } else {
            // If no media is selected, logs a message indicating the same.
            Log.d("Photo Picker", "No media selected")
        }
    }

    /**
     * Starts the camera activity to capture an image.
     *
     * Retrieves the URI for the captured image and launches the camera intent.
     */
    private fun startCamera() {
        // Get the URI for the captured image.
        currentImageUri = getImageUri(this)
        // Launch the camera intent to capture an image.
        launcherIntentCamera.launch(currentImageUri)
    }

    // Activity result launcher for camera capture.
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        // Display the captured image if the capture process is successful.
        if (isSuccess) {
            showImage()
        }
    }

    /**
     * Starts the CameraX activity to capture images.
     */
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    /**
     * Activity result launcher for starting the CameraX activity.
     *
     * It handles the result of the CameraX activity and retrieves the captured image URI.
     */
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            // Retrieve the captured image URI from the result data and display the image.
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    /**
     * Displays the selected image in the ImageView.
     */
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    /**
     * Uploads the selected image to the server.
     *
     * This function retrieves the image URI, converts it to a File, reduces its size, and then
     * uploads it to the server along with a description. It displays a loading indicator while
     * the upload is in progress and shows a toast message upon completion or error.
     */
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = "Ini adalah deksripsi gambar"


            // Before implement repository and injection

//            showLoading(true)
//
//            // Convert description and image file to request body parts
//            val requestBody = description.toRequestBody("text/plain".toMediaType())
//            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
//            val multipartBody = MultipartBody.Part.createFormData(
//                "photo",
//                imageFile.name,
//                requestImageFile
//            )
//
//            lifecycleScope.launch {
//                try {
//                    // Upload image to the server
//                    val apiService = ApiConfig.getApiService()
//                    val successResponse = apiService.uploadImage(multipartBody, requestBody)
//
//                    // Show success message
//                    showToast(successResponse.message)
//                    showLoading(false)
//                } catch (e: HttpException) {
//                    // Handle HTTP error
//                    val errorBody = e.response()?.errorBody()?.string()
//                    val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
//
//                    // Show error message
//                    showToast(errorResponse.message)
//                    showLoading(false)
//                }
//            }


            // after  implement repository and injection
            viewModel.uploadImage(imageFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showToast(result.data.message)
                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }

        } ?: showToast(getString(R.string.empty_image_warning))
    }

    /**
     * Shows or hides the loading indicator based on the isLoading parameter.
     *
     * @param isLoading Boolean indicating whether the loading indicator should be displayed or hidden.
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Displays a toast message with the given message.
     *
     * @param message The message to be displayed in the toast.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        // Required permission for accessing the camera.
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}