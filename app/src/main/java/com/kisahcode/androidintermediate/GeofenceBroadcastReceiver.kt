package com.kisahcode.androidintermediate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

/**
 * BroadcastReceiver to handle geofence transition events and send notifications.
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    /**
     * Handles the broadcast of geofence transition events.
     *
     * When a geofence transition event is received, this method is called to process the event and
     * take appropriate actions. It checks the type of geofence transition (entering or dwelling in
     * a geofence) and triggers the corresponding actions. If there are errors in the geofencing
     * event, such as invalid geofences or permissions, it logs error messages and sends notifications.
     *
     * @param context The context in which the broadcast is received.
     * @param intent The intent containing the geofencing event.
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Check if the broadcast action is for a geofence event
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            // Retrieve the geofencing event from the intent
            val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

            // Check if there's an error in the geofencing event
            if (geofencingEvent.hasError()) {
                // If there's an error, log the error message and send a notification
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                sendNotification(context, errorMessage)
                return
            }

            // Retrieve the type of geofence transition (enter or dwell) from the geofencing event
            val geofenceTransition = geofencingEvent.geofenceTransition

            // Process the geofence transition based on its type
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                // If the transition is entering or dwelling in a geofence, handle the transition
                val geofenceTransitionString = when (geofenceTransition) {
                    Geofence.GEOFENCE_TRANSITION_ENTER -> "Anda telah memasuki area"
                    Geofence.GEOFENCE_TRANSITION_DWELL -> "Anda telah di dalam area"
                    else -> "Invalid transition type"
                }

                // Retrieve the triggering geofences from the geofencing event
                val triggeringGeofences = geofencingEvent.triggeringGeofences

                // Process each triggering geofence
                triggeringGeofences?.forEach { geofence ->
                    // Generate transition details message for logging and notification
                    val geofenceTransitionDetails = "$geofenceTransitionString ${geofence.requestId}"
                    Log.i(TAG, geofenceTransitionDetails)

                    // Send a notification with the geofence transition details
                    sendNotification(context, geofenceTransitionDetails)
                }
            } else {
                // If the geofence transition type is not recognized, log an error and send a notification
                val errorMessage = "Invalid transition type : $geofenceTransition"
                Log.e(TAG, errorMessage)
                sendNotification(context, errorMessage)
            }
        }
    }

    /**
     * Sends a notification to the user based on the geofence transition event.
     *
     * This method creates and displays a notification with the provided geofence transition details.
     *
     * @param context The context in which the notification is sent.
     * @param geofenceTransitionDetails The details of the geofence transition event to be displayed in the notification.
     */
    private fun sendNotification(context: Context, geofenceTransitionDetails: String) {
        // Retrieve the system notification service
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification builder with basic notification details
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(geofenceTransitionDetails)
            .setContentText("Anda sudah bisa absen sekarang :)")
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)

        // If the Android version is Oreo or higher, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = mBuilder.build()

        // Display the notification
        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TAG = "GeofenceBroadcast"
        const val ACTION_GEOFENCE_EVENT = "GeofenceEvent"
        private const val CHANNEL_ID = "1"
        private const val CHANNEL_NAME = "Geofence Channel"
        private const val NOTIFICATION_ID = 1
    }
}