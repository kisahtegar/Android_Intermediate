package com.kisahcode.androidintermediate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.kisahcode.androidintermediate.CameraActivity.Companion.CAMERAX_RESULT
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding


/**
 * MainActivity of the Intent Gallery demonstration application.
 *
 * This activity demonstrates the usage of intent to pick visual media from the device gallery.
 * It allows users to select an image from the gallery and display it in an ImageView.
 * Currently, camera functionalities are not implemented, and uploading the selected image is a placeholder.
 */
class MainActivity : AppCompatActivity() {

    // View binding for the activity layout.
    private lateinit var binding: ActivityMainBinding

    // URI of the currently selected image.
    private var currentImageUri: Uri? = null

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

    private fun uploadImage() {
        Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
    }

    companion object {
        // Required permission for accessing the camera.
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}