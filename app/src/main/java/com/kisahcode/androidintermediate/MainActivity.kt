package com.kisahcode.androidintermediate

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding

/**
 * The main activity of the application.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Variables for bound service
    private var boundStatus = false
    private lateinit var boundService: MyBoundService

    // Service connection object to handle binding to MyBoundService
    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            // Update bound status when the service is disconnected
            boundStatus = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // Retrieve the service instance from the binder
            val myBinder = service as MyBoundService.MyBinder
            boundService = myBinder.getService

            // Update bound status and interact with the service
            boundStatus = true
            getNumberFromService()
        }
    }

    // Activity result launcher for permission requests.
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean? ->
        if (!isGranted!!)
            Toast.makeText(this,
                "Unable to display Foreground service notification due to permission decline",
                Toast.LENGTH_LONG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up service intent to start and stop the background service.
        val serviceIntent = Intent(this, MyBackgroundService::class.java)

        // Set click listeners for buttons to start and stop the background service.
        binding.btnStartBackgroundService.setOnClickListener {
            startService(serviceIntent)
        }
        binding.btnStopBackgroundService.setOnClickListener {
            stopService(serviceIntent)
        }

        // Request permission to display foreground service notification if the device API level is 33 or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED)
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Set up service intent to start and stop the foreground service.
        val foregroundServiceIntent = Intent(this, MyForegroundService::class.java)
        binding.btnStartForegroundService.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(foregroundServiceIntent)
            } else {
                startService(foregroundServiceIntent)
            }
        }
        binding.btnStopForegroundService.setOnClickListener {
            stopService(foregroundServiceIntent)
        }

        // Set up the intent to start the MyBoundService
        val boundServiceIntent = Intent(this, MyBoundService::class.java)
        binding.btnStartBoundService.setOnClickListener {
            // Bind to the MyBoundService and pass the connection object to establish the connection
            bindService(boundServiceIntent, connection, BIND_AUTO_CREATE)
        }
        binding.btnStopBoundService.setOnClickListener {
            // Unbind from the MyBoundService
            unbindService(connection)
        }
    }

    /**
     * Called when the activity is no longer visible to the user.
     *
     * This method is called when the activity enters the "stopped" state, meaning it is no longer
     * visible to the user. It overrides the onStop() method in the superclass to unbind from the
     * MyBoundService if the service is currently bound, and update the boundStatus flag accordingly.
     */
    override fun onStop() {
        super.onStop()

        // Check if the service is currently bound
        if (boundStatus) {
            // Unbind from the bound service and update the boundStatus flag
            unbindService(connection)
            boundStatus = false
        }
    }

    /**
     * Observes changes in numberLiveData and updates the UI accordingly.
     *
     * This function observes changes in the numberLiveData LiveData object of the MyBoundService.
     * Whenever the LiveData object emits a new value, this function is invoked to update the UI
     * with the latest number data retrieved from the bound service.
     */
    private fun getNumberFromService() {
        // Observe changes in the numberLiveData LiveData object
        boundService.numberLiveData.observe(this) { number ->
            // Update the UI with the latest number data
            binding.tvBoundServiceNumber.text = number.toString()
        }
    }
}