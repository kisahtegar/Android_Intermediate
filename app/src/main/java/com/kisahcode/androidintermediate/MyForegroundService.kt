package com.kisahcode.androidintermediate

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Notification.FOREGROUND_SERVICE_IMMEDIATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A foreground service that performs tasks asynchronously.
 */
class MyForegroundService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    /**
     * Called when the service is starting.
     *
     * @param intent The Intent that was used to start the service.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     * @return The return value indicates what semantics the system should use for the service's
     *            current started state.
     */
    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Build the notification for the foreground service.
        val notification = buildNotification()

        // Start the service in the foreground
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        Log.d(MyBackgroundService.TAG, "onStartCommand: Service dijalankan.");

        // Launch a coroutine to perform background tasks.
        serviceScope.launch {
            // Simulate a task that runs for 50 seconds.
            for (i in 1..50) {
                delay(1000)
                Log.d(MyBackgroundService.TAG, "onStartCommand: Do Something $i")
            }

            // Stop the foreground service after the task is completed.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_DETACH)
            }else{
                @Suppress("DEPRECATION")
                stopForeground(true)
            }

            // Stop the service after the task is completed.
            stopSelf()

            Log.d(MyBackgroundService.TAG, "onStartCommand: Service dihentikan.");
        }

        // Indicate that the service should continue running until explicitly stopped.
        return START_STICKY
    }

    /**
     * Called when the service is no longer used and is being destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        // Cancel the service job when the service is destroyed.
        serviceJob.cancel()
        Log.d(MyBackgroundService.TAG, "onDestroy: Service dihentikan.")
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Builds a notification for the foreground service.
     *
     * This function creates and configures a notification to be displayed while the foreground service is running.
     * It sets up an intent to launch the MainActivity when the notification is clicked. The notification includes
     * a title, content text, and a small icon. For Android 12 and later, it configures the foreground service
     * behavior. For devices running Android Oreo (API level 26) and later, it creates a notification channel
     * with the specified channel ID and description.
     *
     * @return The notification object.
     */
    private fun buildNotification(): Notification {
        // Create an intent to launch the MainActivity when the notification is clicked.
        val notificationIntent = Intent(this, MainActivity::class.java)

        // Set up flags for the pending intent based on the Android version.
        val pendingFlags: Int = if (Build.VERSION.SDK_INT >= 23) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, pendingFlags)

        // Get the NotificationManager system service.
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification builder with the specified attributes.
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Saat ini foreground service sedang berjalan.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)

        // Set foreground service behavior for Android 12 and later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            notificationBuilder.setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
        }

        // Create a notification channel for devices running Android Oreo (API level 26) and later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            // Set the channel description.
            channel.description = CHANNEL_NAME

            // Set the notification channel ID and create the notification channel.
            notificationBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        // Build and return the notification.
        return notificationBuilder.build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "dicoding channel"
        internal val TAG = MyForegroundService::class.java.simpleName
    }
}