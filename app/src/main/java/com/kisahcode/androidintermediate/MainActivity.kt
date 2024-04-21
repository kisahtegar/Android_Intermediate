package com.kisahcode.androidintermediate

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupData()
    }

    /**
     * Set up the click action for the settings icon.
     */
    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    /**
     * Set up the view configurations for the activity.
     *
     * This function handles adjustments related to the status bar and action bar visibility based
     * on the Android version.
     */
    private fun setupView() {
        // Check if the device is running Android R or later.
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // If the device is running Android R or later, use WindowInsetsController to hide the status bar.
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            // For Android versions prior to Android R, use deprecated methods to hide the status bar.
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Hide the action bar.
        supportActionBar?.hide()
    }

    /**
     * Set up the data for the activity.
     *
     * This function retrieves product details from the remote data source and populates the UI
     * elements with the retrieved data, applying formatting where necessary.
     * Additionally, it sets up accessibility information for UI elements.
     */
    private fun setupData() {
        // Initialize the repository to fetch product details.
        val repository = RemoteDataSource(this)
        // Retrieve product details from the remote data source.
        val product = repository.getDetailProduct().apply {
            // Populate UI elements with the retrieved product details.
            binding.apply {
                previewImageView.setImageResource(image)
                nameTextView.text = name
                storeTextView.text = store
                colorTextView.text = color
                sizeTextView.text = size
                descTextView.text = desc
                // Format and set the price, date, and rating of the product.
                priceTextView.text = price.withCurrencyFormat()
                dateTextView.text = getString(R.string.dateFormat, date.withDateFormat())
                ratingTextView.text = getString(R.string.ratingFormat, rating.withNumberingFormat(), countRating.withNumberingFormat())
            }
        }

        // Set up accessibility information for UI elements.
        setupAccessibility(product)
    }

    /**
     * Set up accessibility information for UI elements.
     *
     * @param productModel The product model containing information about the product.
     */
    private fun setupAccessibility(productModel: ProductModel) {
        productModel.apply {
            binding.apply {
                // Set content descriptions for UI elements to improve accessibility.
                settingImageView.contentDescription = getString(R.string.settingDescription)
                previewImageView.contentDescription = getString(R.string.previewDescription)
                colorTextView.contentDescription = getString(R.string.colorDescription, color)
                sizeTextView.contentDescription = getString(R.string.sizeDescription, size)
                ratingTextView.contentDescription = getString(
                    R.string.ratingDescription,
                    rating.withNumberingFormat(),
                    countRating.withNumberingFormat()
                )
                storeTextView.contentDescription = getString(R.string.storeDescription, store)
            }
        }
    }
}