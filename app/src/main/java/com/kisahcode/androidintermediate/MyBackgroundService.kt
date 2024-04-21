package com.kisahcode.androidintermediate

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A background service that performs tasks asynchronously.
 */
class MyBackgroundService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    /**
     * Called when the service is starting.
     *
     * @param intent The Intent that was used to start the service.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     * @return The return value indicates what semantics the system should use for the service's
     *         current started state.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: Service dijalankan.");

        // Launch a coroutine to perform background tasks.
        serviceScope.launch {
            // Simulate a task that runs for 10 seconds.
            for (i in 1..10) {
                delay(1000)
                Log.d(TAG, "onStartCommand: Do Something $i")
            }

            // Stop the service after the task is completed.
            stopSelf()

            Log.d(TAG, "onStartCommand: Service dihentikan.");
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
        Log.d(TAG, "onDestroy: Service dihentikan.")
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        internal val TAG = MyBackgroundService::class.java.simpleName
    }
}