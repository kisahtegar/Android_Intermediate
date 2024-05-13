package com.kisahcode.androidintermediate

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding
import java.util.Date

/**
 * MainActivity represents the main screen of the application where users interact with various
 * features and functionalities.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: FirebaseMessageAdapter

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

        // Initialize Firebase Realtime Database
        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        // Set up functionality to send messages to the database
        binding.sendButton.setOnClickListener {
            // Create a Message object with the entered message, user's display name, photo URL, and current timestamp
            val friendlyMessage = Message(
                binding.messageEditText.text.toString(),
                firebaseUser.displayName.toString(),
                firebaseUser.photoUrl.toString(),
                Date().time
            )

            // Push the message to the database
            messagesRef.push().setValue(friendlyMessage) { error, _ ->
                if (error != null) {
                    // Display a toast message if there is an error sending the message
                    Toast.makeText(this, getString(R.string.send_error) + error.message, Toast.LENGTH_SHORT).show()
                } else {
                    // Display a toast message if the message is sent successfully
                    Toast.makeText(this, getString(R.string.send_success), Toast.LENGTH_SHORT).show()
                }
            }

            // Clear the message text field after sending the message
            binding.messageEditText.setText("")
        }

        // Configure RecyclerView to display messages from the database
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messagesRef, Message::class.java)
            .build()
        adapter = FirebaseMessageAdapter(options, firebaseUser.displayName)
        binding.messageRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        adapter.stopListening()
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

    companion object {
        const val MESSAGES_CHILD = "messages"
    }
}