package com.kisahcode.androidintermediate

import android.media.SoundPool
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * MainActivity of the SoundPool demonstration application.
 *
 * This activity demonstrates the usage of SoundPool to play short audio files efficiently.
 * It loads a sound file into SoundPool and plays it when a certain action is triggered.
 */
class MainActivity : AppCompatActivity() {

    // SoundPool object for handling audio playback.
    private lateinit var sp: SoundPool

    // ID of the loaded sound file.
    private var soundId: Int = 0

    // Flag indicating whether the sound file is loaded successfully.
    private var spLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the button for playing the sound.
        val btnSound = findViewById<Button>(R.id.btn_sound_pool)

        // Initialize SoundPool with a maximum of 10 streams.
        sp = SoundPool.Builder()
            .setMaxStreams(10)
            .build()

        // Set up a listener to check if the sound is loaded successfully.
        sp.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                // Flag indicating successful loading of the sound.
                spLoaded = true
            } else {
                // Display a toast message if sound loading fails.
                Toast.makeText(this@MainActivity, "Gagal load", Toast.LENGTH_SHORT).show()
            }
        }

        // Load the sound file into SoundPool and obtain its sound ID.
        soundId = sp.load(this, R.raw.clinking_glasses, 1)

        // Set click listener for the sound button.
        btnSound.setOnClickListener {
            // Play the sound if it is loaded successfully.
            if (spLoaded) {
                sp.play(soundId, 1f, 1f, 0, 0, 1f)
            }
        }
    }
}