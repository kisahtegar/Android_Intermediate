package com.kisahcode.androidintermediate

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun startCamera() {
        Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
    }

    private fun startCameraX() {
        Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
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
}