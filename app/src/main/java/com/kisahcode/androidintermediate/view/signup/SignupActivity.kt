package com.kisahcode.androidintermediate.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.databinding.ActivitySignupBinding

/**
 * Activity for user signup.
 */
class SignupActivity : AppCompatActivity() {

    // View binding instance for the activity's layout
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
     * In this case, sets up a click listener for the signup button.
     */
    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()

            // Display a confirmation dialog for successful signup
            AlertDialog.Builder(this).apply {
                setTitle("Yeah!")
                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                setPositiveButton("Lanjut") { _, _ ->
                    // Finish the SignupActivity to prevent the user from going back to it
                    finish()
                }

                // Create and show the AlertDialog
                create()
                show()
            }
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

        // Alpha animations for various UI elements to fade them in sequentially
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        // Create an AnimatorSet to play the animations sequentially with a delay
        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100 // Add a delay before starting the animations
        }.start() // Start the animation
    }
}