package com.kisahcode.androidintermediate.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding
import com.kisahcode.androidintermediate.view.ViewModelFactory
import com.kisahcode.androidintermediate.view.welcome.WelcomeActivity

/**
 * The main activity of the application, responsible for displaying the main user interface.
 */
class MainActivity : AppCompatActivity() {

    // ViewModel instance obtained using the by viewModels delegate
    private val viewModel by viewModels<MainViewModel> {
        // Obtain ViewModelFactory instance using ViewModelFactory's companion object method
        ViewModelFactory.getInstance(this)
    }

    // View binding instance for the activity's layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe changes in user session data using LiveData
        viewModel.getSession().observe(this) { user ->
            // Check if the user is not logged in
            if (!user.isLogin) {
                // If not logged in, navigate to the welcome activity and finish this activity
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        // Setup UI elements and animations
        setupView()
        setupAction()
        playAnimation()
    }

    /**
     * Sets up the view configuration for the activity.
     *
     * This includes hiding the status bar for full-screen mode and hiding the action bar.
     */
    private fun setupView() {
        // Check if the device's API level is at least R (Android 11)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // On Android 11 and above, use WindowInsetsController to hide the status bar
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            // For devices below Android 11, use flags to make the activity full-screen
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        // Hide the action bar (title bar) for a cleaner UI
        supportActionBar?.hide()
    }

    /**
     * Sets up action listeners for UI elements.
     *
     * In this case, sets up a click listener for the logout button.
     */
    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    /**
     * Plays animations to enhance the UI.
     *
     * This includes translation and alpha animations for specific UI elements.
     */
    private fun playAnimation() {
        // Translation animation for the image view to create a subtle movement effect
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000 // Set the duration of the animation to 6 seconds
            repeatCount = ObjectAnimator.INFINITE // Set the repeat count to infinite
            repeatMode = ObjectAnimator.REVERSE // Reverse the animation when it reaches the end
        }.start() // Start the animation

        // Sequential alpha animations for text views and logout button to gradually fade them in
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(100)

        // Create an AnimatorSet to play the alpha animations sequentially
        AnimatorSet().apply {
            playSequentially(name, message, logout)
            startDelay = 100 // Delay the start of the animation by 100 milliseconds
        }.start() // Start the animation
    }
}