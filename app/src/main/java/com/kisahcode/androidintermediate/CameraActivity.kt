package com.kisahcode.androidintermediate

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.kisahcode.androidintermediate.databinding.ActivityCameraBinding

/**
 * An activity for capturing images using CameraX.
 *
 * This activity allows users to switch between front and back cameras, capture images, and receive
 * the captured image URI as a result.
 */
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the camera switch button to toggle between front and back cameras.
        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

        // Set click listener for the image capture button.
        binding.captureImage.setOnClickListener { takePhoto() }
    }

    /**
     * Resumes the activity.
     *
     * Hides system UI elements, such as the status bar, and starts the camera.
     */
    override fun onResume() {
        super.onResume()
        // Hide system UI elements to provide a full-screen experience.
        hideSystemUI()
        // Start the camera.
        startCamera()
    }

    /**
     * Starts the camera to enable image capture.
     *
     * Uses CameraX API to bind the camera to the lifecycle of this activity.
     */
    private fun startCamera() {
        // Get an instance of the camera provider.
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        // Add a listener to the camera provider future.
        cameraProviderFuture.addListener({
            // Retrieve the camera provider from the future.
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Create a preview use case.
            val preview = Preview.Builder()
                .build()
                .also {
                    // Set the surface provider for the preview.
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Create an image capture use case.
            imageCapture = ImageCapture.Builder().build()

            try {
                // Unbind all use cases from the camera provider.
                cameraProvider.unbindAll()
                // Bind the preview and image capture use cases to the lifecycle of this activity.
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                // Handle exceptions that occur when starting the camera.
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * Captures a photo using the current image capture configuration.
     *
     * Saves the captured image to a temporary file and returns the URI of the saved image.
     */
    private fun takePhoto() {
        // Check if the image capture use case is initialized.
        val imageCapture = imageCapture ?: return

        // Create a temporary file to store the captured image.
        val photoFile = createCustomTempFile(application)

        // Configure the output options for saving the captured image.
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Take a picture using the image capture use case.
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // If the image is saved successfully, return the URI of the saved image.
                    val intent = Intent()
                    intent.putExtra(EXTRA_CAMERAX_IMAGE, output.savedUri.toString())
                    setResult(CAMERAX_RESULT, intent)
                    finish()
                }

                override fun onError(exc: ImageCaptureException) {
                    // Handle errors that occur during image capture.
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    /**
     * Hides system UI elements such as the status bar and action bar to provide a fullscreen experience.
     */
    private fun hideSystemUI() {
        // Check if the device is running on Android R (API level 30) or higher.
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use the WindowInsetsController to hide the status bar.
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            // For older Android versions, set flags to enable fullscreen mode.
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        // Hide the action bar if present.
        supportActionBar?.hide()
    }

    /**
     * Lazy initialization of an OrientationEventListener to handle changes in device orientation.
     *
     * Adjusts the target rotation of the ImageCapture based on the device's orientation.
     */
    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                // Check if the orientation is unknown.
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                // Determine the rotation based on the orientation angle.
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                // Adjust the target rotation of the ImageCapture accordingly.
                imageCapture?.targetRotation = rotation
            }
        }
    }

    /**
     * Lifecycle callback invoked when the activity is starting.
     *
     * Enables the orientation change listener to adjust the target rotation of the ImageCapture.
     */
    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    /**
     * Lifecycle callback invoked when the activity is stopping.
     *
     * Disables the orientation change listener to conserve resources.
     */
    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    companion object {
        // Tag for logging purposes.
        private const val TAG = "CameraActivity"
        // Extra key for passing the captured image URI.
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        // Request code for the result of the CameraX activity.
        const val CAMERAX_RESULT = 200
    }
}