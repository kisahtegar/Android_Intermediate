package com.kisahcode.androidintermediate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding

/**
 * MainActivity of the ExoPlayer demonstration application.
 *
 * This activity demonstrates the usage of ExoPlayer to play video and audio media items simultaneously.
 * It sets up ExoPlayer with video and audio MediaItems, prepares it for playback, and displays the
 * video in a PlayerView.
 */
class MainActivity : AppCompatActivity() {

    // View binding for the activity layout.
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create a MediaItem for the video from the provided URI.
        val videoItem = MediaItem.fromUri("https://github.com/dicodingacademy/assets/releases/download/release-video/VideoDicoding.mp4")

        // Create a MediaItem for the audio from the provided URI.
        val audioItem = MediaItem.fromUri("https://github.com/dicodingacademy/assets/raw/main/android_intermediate_academy/bensound_ukulele.mp3")

        // Create an ExoPlayer instance.
        val player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            // Set the media items to be played by the ExoPlayer.
            exoPlayer.setMediaItem(videoItem)
            exoPlayer.addMediaItem(audioItem)
            // Prepare the ExoPlayer for playback.
            exoPlayer.prepare()
        }

        // Attach the ExoPlayer to the PlayerView to display the video.
        binding.playerView.player = player

        // Hide system UI elements such as the status bar and navigation bar.
        hideSystemUI()
    }

    /**
     * Hides system UI elements such as the status bar and navigation bar.
     */
    private fun hideSystemUI() {
        // Set the activity window to not fit system windows to allow content to be displayed behind system bars.
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Get the WindowInsetsControllerCompat associated with the activity window and the PlayerView.
        WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
            // Hide system bars (status bar and navigation bar).
            controller.hide(WindowInsetsCompat.Type.systemBars())
            // Set the system bars behavior to show transient bars by swipe.
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}