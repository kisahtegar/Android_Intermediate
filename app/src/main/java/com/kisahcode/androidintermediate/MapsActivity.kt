package com.kisahcode.androidintermediate

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kisahcode.androidintermediate.databinding.ActivityMapsBinding

/**
 * Activity to demonstrate Geofencing functionality using Google Maps.
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var geofencingClient: GeofencingClient

    // Coordinates for the center of the geofence area
    private val centerLat = 37.4274745
    private val centerLng = -122.169719
    // Radius of the geofence area in meters
    private val geofenceRadius = 400.0

    /**
     * Lazily initializes a PendingIntent for handling geofence transition events.
     *
     * This PendingIntent is used to send geofence transition events to the GeofenceBroadcastReceiver.
     * @return The PendingIntent instance for handling geofence events.
     */
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = GeofenceBroadcastReceiver.ACTION_GEOFENCE_EVENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    // Launcher for requesting notification permission (for API level 33 and higher)
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request notification permission for API level 33 and higher
        if (Build.VERSION.SDK_INT >= 33) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Callback method invoked when the map is ready to be used.
     *
     * This method initializes the GoogleMap object and configures it with necessary settings
     * such as zoom controls, marker, and geofence area. It also checks and requests location
     * permissions to enable My Location feature on the map.
     *
     * @param googleMap The GoogleMap instance representing the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Enable zoom controls on the map
        mMap.uiSettings.isZoomControlsEnabled = true

        // Define the center point of the geofence area
        val stanford = LatLng(centerLat, centerLng)

        // Add a marker for Stanford University
        mMap.addMarker(MarkerOptions().position(stanford).title("Stanford University"))

        // Move the camera to the center point of the geofence area with a zoom level of 15
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stanford, 15f))

        // Add a circular overlay to represent the geofence area
        mMap.addCircle(
            CircleOptions()
                .center(stanford)
                .radius(geofenceRadius)
                .fillColor(0x22FF0000) // Red with 34% opacity
                .strokeColor(Color.RED)
                .strokeWidth(3f)
        )

        // Check and request location permissions to enable My Location feature
        getMyLocation()

        addGeofence()
    }

    // Launcher for requesting background location permission (for API level 29 and higher)
    private val requestBackgroundLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Background location permission granted or not required
                getMyLocation()
            }
        }

    // Flag indicating whether the device is running Android Q (API level 29) or later
    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    // Launcher for requesting foreground location permission
    @TargetApi(Build.VERSION_CODES.Q)
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Foreground location permission granted or not required
                if (runningQOrLater) {
                    // For Android Q (API level 29) and later, request background location permission
                    requestBackgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    // For pre-Android Q devices, proceed to get current location
                    getMyLocation()
                }
            }
        }

    /**
     * Checks if the specified permission is granted.
     *
     * @param permission The permission to check.
     * @return True if the permission is granted, false otherwise.
     */
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks if both foreground and background location permissions are granted.
     *
     * @return True if both foreground and background location permissions are granted, false otherwise.
     */
    @TargetApi(Build.VERSION_CODES.Q)
    private fun checkForegroundAndBackgroundLocationPermission(): Boolean {
        // Check if foreground location permission is granted
        val foregroundLocationApproved = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        // Check if background location permission is granted (for Android Q and later)
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                // For pre-Android Q devices, consider background permission as granted
                true
            }

        // Return true if both foreground and background permissions are granted, false otherwise
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    /**
     * Enables My Location feature on the map by checking and requesting location permissions.
     *
     * If permissions are granted, My Location is enabled; otherwise, permission requests are initiated.
     */
    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkForegroundAndBackgroundLocationPermission()) {
            // If both foreground and background location permissions are granted
            mMap.isMyLocationEnabled = true
        } else {
            // Request location permission if not granted
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Adds a geofence for the specified location.
     *
     * This method creates a Geofence object with the given parameters and adds it to the geofencing
     * client. It then removes any existing geofences associated with the provided PendingIntent and
     * adds the new geofence.
     *
     * @see showToast
     */
    @SuppressLint("MissingPermission")
    private fun addGeofence() {
        // Obtain the GeofencingClient instance
        geofencingClient = LocationServices.getGeofencingClient(this)

        // Create a Geofence object with the specified parameters
        val geofence = Geofence.Builder()
            .setRequestId("kampus")
            .setCircularRegion(
                centerLat, // Latitude of the geofence center
                centerLng, // Longitude of the geofence center
                geofenceRadius.toFloat() // Radius of the geofence in meters
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE) // Geofence never expires
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER) // Geofence transition types
            .setLoiteringDelay(5000) // Set the loitering delay in milliseconds
            .build()

        // Create a GeofencingRequest with the specified initial trigger and add the geofence
        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER) // Initial trigger when entering the geofence
            .addGeofence(geofence)
            .build()

        // Remove any existing geofences associated with the PendingIntent
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            // Once removal is complete, add the new geofence
            addOnCompleteListener {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                    // Handle success and failure cases

                    addOnSuccessListener {
                        showToast("Geofencing added")
                    }
                    addOnFailureListener {
                        showToast("Geofencing not added : ${it.message}")
                        Log.d("MapsActivity", "Geofencing not added : ${it.message}")
                    }
                }
            }
        }
    }

    /**
     * Displays a short toast message with the given text.
     *
     * @param text The text to be displayed in the toast message.
     */
    private fun showToast(text: String) {
        Toast.makeText(this@MapsActivity, text, Toast.LENGTH_SHORT).show()
    }
}