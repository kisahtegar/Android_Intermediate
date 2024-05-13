package com.kisahcode.androidintermediate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.kisahcode.androidintermediate.databinding.ActivityLoginBinding
import com.google.firebase.ktx.Firebase

/**
 * Activity responsible for handling user login using Firebase authentication.
 * It provides Google Sign-In functionality for the user to log in.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Set click listener for sign-in button
        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

    /**
     * Initiates the Google Sign-In flow.
     *
     * This function starts the Google Sign-In flow by launching the sign-in intent provided by the
     * GoogleSignInClient. The result of the sign-in operation is handled by the resultLauncher,
     * which invokes firebaseAuthWithGoogle upon successful retrieval of the Google Sign-In account
     * information.
     */
    private fun signIn() {
        // Create a sign-in intent using the GoogleSignInClient
        val signInIntent = googleSignInClient.signInIntent
        // Launch the sign-in intent using the resultLauncher
        resultLauncher.launch(signInIntent)
    }

    // Activity Result launcher for handling Google Sign-In result
    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    /**
     * Authenticates the user with Firebase using the provided Google Sign-In ID token.
     *
     * This function initiates the authentication process with Firebase by exchanging the Google
     * Sign-In ID token for Firebase credentials. Upon successful authentication, the function retrieves
     * the user's information from Firebase and updates the UI accordingly. If authentication fails,
     * an appropriate error message is logged.
     *
     * @param idToken The Google Sign-In ID token obtained from the authentication result.
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        // Create credentials using the Google Sign-In ID token
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        // Sign in to Firebase using the provided credentials
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    // Retrieve the current user's information
                    val user = auth.currentUser
                    // Update the UI with the signed-in user's information
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    /**
     * Updates the user interface based on the current authentication status.
     *
     * This function is responsible for managing the transition between the LoginActivity and the
     * MainActivity based on the authentication status of the user. If a user is authenticated, it
     * launches the MainActivity and finishes the LoginActivity to prevent the user from navigating
     * back to the login screen. If no user is authenticated, it simply returns without performing
     * any UI updates.
     *
     * @param currentUser The currently authenticated FirebaseUser, or null if no user is authenticated.
     */
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}