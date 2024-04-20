package com.kisahcode.androidintermediate.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kisahcode.androidintermediate.data.pref.UserModel
import com.kisahcode.androidintermediate.databinding.ActivityLoginBinding
import com.kisahcode.androidintermediate.view.ViewModelFactory
import com.kisahcode.androidintermediate.view.main.MainActivity

/**
 * Activity responsible for handling the login process.
 */
class LoginActivity : AppCompatActivity() {

    // View model instance for handling login-related operations
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    // View binding instance for accessing views in the layout
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
     * Sets up the actions for UI elements, such as button clicks.
     *
     * When the login button is clicked, it retrieves the email entered by the user,
     * saves the session using the ViewModel, and displays a dialog to inform the user about successful login.
     * Upon pressing the "Lanjut" button in the dialog, it navigates the user to the main activity.
     */
    private fun setupAction() {
        // Set up a click listener for the login button
        binding.loginButton.setOnClickListener {
            // Retrieve the email entered by the user from the email EditText
            val email = binding.emailEditText.text.toString()

            // Save the session using the ViewModel by passing a UserModel with the email and a sample token
            viewModel.saveSession(UserModel(email, "sample_token"))

            // Display an AlertDialog to inform the user about successful login
            AlertDialog.Builder(this).apply {
                setTitle("Yeah!")
                setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    // Set flags to clear the task stack and create a new task
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    // Finish the LoginActivity to prevent the user from going back to it
                    finish()
                }

                // Create and show the AlertDialog
                create()
                show()
            }
        }
    }

    /**
     * Plays animations for UI elements to enhance the login screen.
     *
     * It includes a translation animation for the image view and sequential alpha animations for
     * text views and buttons.
     */
    private fun playAnimation() {
        // Translate animation for the image view to create a subtle movement effect
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000 // Set the duration of the animation to 6 seconds
            repeatCount = ObjectAnimator.INFINITE // Set the repeat count to infinite
            repeatMode = ObjectAnimator.REVERSE // Reverse the animation when it reaches the end
        }.start() // Start the animation

        // Alpha animations for various UI elements to gradually fade them in
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        // Create an AnimatorSet to play the alpha animations sequentially
        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100 // Delay the start of the animation by 100 milliseconds
        }.start() // Start the animation
    }
}