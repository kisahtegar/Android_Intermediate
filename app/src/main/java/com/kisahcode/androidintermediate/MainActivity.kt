package com.kisahcode.androidintermediate

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding

/**
 * MainActivity represents the main screen of the application where users interact with various
 * features and functionalities.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase authentication
        auth = Firebase.auth

        // Check if there is a currently authenticated user
        val firebaseUser = auth.currentUser

        // If no user is authenticated, launch the LoginActivity and finish the MainActivity
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Signs out the current user from Firebase authentication.
     *
     * This function revokes the authentication token associated with the current user, effectively
     * signing them out. It then navigates the user back to the LoginActivity while finishing
     * the MainActivity to ensure a clean transition.
     */
    private fun signOut() {
        // Revokes the authentication token associated with the current user
        auth.signOut()

        // Navigates the user back to the LoginActivity and finishes the MainActivity
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}