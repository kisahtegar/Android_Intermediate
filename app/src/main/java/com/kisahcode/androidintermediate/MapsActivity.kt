package com.kisahcode.androidintermediate

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

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

    // Coordinates for the center of the geofence area
    private val centerLat = 37.4274745
    private val centerLng = -122.169719
    // Radius of the geofence area in meters
    private val geofenceRadius = 400.0

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
}