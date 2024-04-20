package com.kisahcode.androidintermediate.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.databinding.ActivityWelcomeBinding
import com.kisahcode.androidintermediate.view.login.LoginActivity
import com.kisahcode.androidintermediate.view.signup.SignupActivity


/**
 * The welcome activity of the application, displayed to users who are not logged in.
 */
class WelcomeActivity : AppCompatActivity() {

    // View binding instance for the activity's layout
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
     * In this case, sets up click listeners for the login and signup buttons.
     */
    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            // Start the LoginActivity when the login button is clicked
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            // Start the SignupActivity when the signup button is clicked
            startActivity(Intent(this, SignupActivity::class.java))
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

        // Alpha animations for the login and signup buttons, title, and description text views
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(100)

        // Create an AnimatorSet to play the login and signup button animations together
        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        // Create another AnimatorSet to play the title, description, and button animations sequentially
        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start() // Start the animation
        }
    }
}