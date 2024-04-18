package com.kisahcode.androidintermediate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.Toast

/**
 * The main activity of the application.
 *
 * This activity is responsible for initializing the user interface and handling user interactions.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        // Set the layout for this activity
        setContentView(R.layout.activity_main)

        // Initialize views
        val seatView = findViewById<SeatsView>(R.id.seatsView)
        val button = findViewById<Button>(R.id.finishButton)

        // Set click listener for the button
        button.setOnClickListener {
            // Display a toast message based on whether a seat is selected or not
            seatView.seat?.let {
                Toast.makeText(this, "Kursi Anda nomor ${it.name}.", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "Silakan pilih kursi terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}