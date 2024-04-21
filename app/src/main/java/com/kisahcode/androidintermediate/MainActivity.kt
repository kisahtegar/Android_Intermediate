package com.kisahcode.androidintermediate

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

/**
 * MainActivity of the MediaPlayer demonstration application.
 *
 * This activity illustrates the usage of MediaPlayer to play audio files.
 * It initializes a MediaPlayer object, loads an audio file, and provides controls
 * to play and stop the audio.
 */
class MainActivity : AppCompatActivity() {

    // MediaPlayer object for managing audio playback.
    private var mMediaPlayer: MediaPlayer? = null
    // Flag indicating whether the MediaPlayer is ready to play.
    private var isReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the buttons responsible for controlling audio playback.
        val btnPlay = findViewById<Button>(R.id.btn_play)
        val btnStop = findViewById<Button>(R.id.btn_stop)

        // Set click listener for the play button.
        btnPlay.setOnClickListener {
            if (!isReady) {
                // Prepare the MediaPlayer asynchronously if it's not ready.
                mMediaPlayer?.prepareAsync()
            } else {
                // Toggle between play and pause based on the current playback state.
                if (mMediaPlayer?.isPlaying as Boolean) {
                    mMediaPlayer?.pause()
                } else {
                    mMediaPlayer?.start()
                }
            }
        }

        // Set click listener for the stop button.
        btnStop.setOnClickListener {
            // Stop playback and reset the ready state.
            if (mMediaPlayer?.isPlaying as Boolean || isReady) {
                mMediaPlayer?.stop()
                isReady = false
            }
        }

        // Initialize the MediaPlayer.
        init()
    }

    /**
     * Initializes the MediaPlayer.
     *
     * This function sets up the MediaPlayer object by configuring its audio attributes,
     * setting the data source to the audio file, and preparing it for playback asynchronously.
     */
    private fun init() {
        // Create a new instance of MediaPlayer.
        mMediaPlayer = MediaPlayer()

        // Configure the audio attributes for the MediaPlayer.
        val attribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        // Set the audio attributes for the MediaPlayer.
        mMediaPlayer?.setAudioAttributes(attribute)

        // Load the audio file from the application's resources.
        val afd = applicationContext.resources.openRawResourceFd(R.raw.guitar_background)
        try {
            // Set the data source for the MediaPlayer using the file descriptor, start offset, and length.
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: IOException) {
            // Print the stack trace if an IOException occurs while setting the data source.
            e.printStackTrace()
        }

        // Set a listener to start playback when the MediaPlayer is prepared.
        mMediaPlayer?.setOnPreparedListener {
            // Set the ready flag to true indicating that the MediaPlayer is prepared for playback.
            isReady = true
            // Start playback when the MediaPlayer is prepared.
            mMediaPlayer?.start()
        }

        // Set an error listener to handle any errors that occur during playback.
        mMediaPlayer?.setOnErrorListener { _, _, _ -> false }
    }
}