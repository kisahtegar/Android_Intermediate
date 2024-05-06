package com.kisahcode.androidintermediate

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.kisahcode.androidintermediate.databinding.ActivityMapsBinding
import java.util.concurrent.TimeUnit

/**
 * This activity displays a Google Map and shows the user's last known location on the map.
 * It also handles location permission requests and updates.
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var isTracking = false
    private var allLatLng = ArrayList<LatLng>()
    private var boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     *
     * This callback is triggered when the map is ready to be used. This is where we can add markers
     * or lines, add listeners or move the camera. In this case, we just add a marker near Sydney,
     * Australia. If Google Play services is not installed on the device, the user will be prompted
     * to install it inside the SupportMapFragment. This method will only be triggered once the user
     * has installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //getMyLastLocation()

        // Initialize location request and callback
        createLocationRequest()
        createLocationCallback()

        // Set up UI elements and click listener for tracking button
        binding.btnStart.setOnClickListener {
            if (!isTracking) {
                clearMaps()
                updateTrackingStatus(true)
                startLocationUpdates()
            } else {
                updateTrackingStatus(false)
                stopLocationUpdates()
            }
        }
    }

    /**
     * Activity result launcher to handle location permission requests.
     *
     * This launcher registers an activity result callback for requesting fine and coarse location
     * permissions. When the result is received, it checks the permissions granted by the user.
     * If either fine or coarse location permission is granted, it invokes the getMyLastLocation
     * method to retrieve the user's last known location.
     *
     * @see getMyLastLocation
     */
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // Check if either fine or coarse location permission is granted
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted. Invoke getMyLastLocation method
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Approximate location access granted. Invoke getMyLastLocation method
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    /**
     * Checks if the given permission is granted.
     *
     * This method checks if the specified permission is granted by the user. It returns true if
     * the permission is granted, and false otherwise.
     *
     * @param permission The permission to check.
     * @return True if the permission is granted, false otherwise.
     */
    private fun checkPermission(permission: String): Boolean {
        // Check if the specified permission is granted
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Retrieves the user's last known location.
     *
     * This method checks if the necessary location permissions (fine and coarse) are granted.
     * If both permissions are granted, it retrieves the user's last known location using the
     * FusedLocationProviderClient. If permissions are not granted, it requests location permissions
     * using the requestPermissionLauncher. Upon receiving the location, it calls the showStartMarker
     * method to display a marker on the map representing the user's last known location. If the
     * location is not available, it displays a toast message indicating that the location is not
     * found.
     */
    private fun getMyLastLocation() {
        // Check if both fine and coarse location permissions are granted
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            // Request the user's last known location using FusedLocationProviderClient
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // Check if location is available
                if (location != null) {
                    // Display a marker on the map representing the user's last known location
                    showStartMarker(location)
                } else {
                    // Display a toast message indicating that the location is not found
                    Toast.makeText(
                        this@MapsActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            // If location permissions are not granted, request permissions using requestPermissionLauncher
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    /**
     * Displays a marker on the map representing the user's last known location.
     *
     * This method creates a marker on the Google Map at the specified location. It takes a Location
     * object containing latitude and longitude coordinates and adds a marker to the map with a title
     * indicating the start point. Additionally, it adjusts the camera position to focus on the marker
     * location with a specified zoom level.
     *
     * @param location The user's last known location as a Location object.
     */
    private fun showStartMarker(location: Location) {
        // Convert the Location object to a LatLng object representing the marker's position
        val startLocation = LatLng(location.latitude, location.longitude)

        // Add a marker to the map at the specified location with a title indicating the start point
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title(getString(R.string.start_point))
        )

        // Move the camera to focus on the marker location with a specified zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
    }

    /**
     * Activity result launcher to handle location settings resolution.
     *
     * This launcher registers an activity result callback for handling location settings resolution.
     * When the result is received, it checks the resultCode to determine if the location settings
     * are satisfied or canceled by the user. If the location settings are satisfied, it logs a message
     * indicating that all location settings are satisfied. If the user cancels the resolution
     * (e.g., by dismissing the dialog), it displays a toast message informing the user to enable
     * GPS to use the app.
     */
    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                // Location settings are satisfied
                RESULT_OK ->
                    Log.i(TAG, "onActivityResult: All location settings are satisfied.")
                // User canceled the resolution (e.g., dismissed the dialog)
                RESULT_CANCELED ->
                    Toast.makeText(
                        this@MapsActivity,
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    /**
     * Creates a location request with high accuracy and checks location settings.
     *
     * This method creates a LocationRequest object for real-time location updates with high accuracy.
     * It checks if the device's location settings meet the requirements specified in the request.
     * If the settings are satisfied, it retrieves the user's last known location; otherwise, it launches
     * a resolution intent to prompt the user to enable location settings. Requests high-accuracy
     * location updates with a 1-second interval. Verifies if the device's location settings meet
     * the criteria. If not, prompts the user to enable location settings.
     */
    @Suppress("DEPRECATION")
    private fun createLocationRequest() {
        // Create a LocationRequest object with high accuracy and 1-second interval
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1) // Set location update interval to 1 second
            maxWaitTime = TimeUnit.SECONDS.toMillis(1) // Set maximum wait time for location updates to 1 second
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Request high accuracy location updates
        }

        // Build a LocationSettingsRequest to check if the device's location settings meet the requirements
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)

        // Check the device's location settings
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                // If location settings are satisfied, immediately retrieve the user's last known location
                getMyLastLocation()
            }
            .addOnFailureListener { exception ->
                // If location settings are not satisfied, launch a resolution intent
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Handle errors while launching the resolution intent
                        Toast.makeText(this@MapsActivity, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    /**
     * Creates a location callback to receive location updates.
     *
     * This method defines a LocationCallback object to handle location updates. When the location
     * updates are received, it logs the latitude and longitude for debugging purposes. It also draws
     * a polyline connecting all received locations on the map and adjusts the camera to include
     * the new location.
     */
    private fun createLocationCallback() {
        // Define a LocationCallback object to handle location updates
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // Iterate through the list of received locations
                for (location in locationResult.locations) {
                    // Log the latitude and longitude of each location for debugging
                    Log.d(TAG, "onLocationResult: " + location.latitude + ", " + location.longitude)

                    val lastLatLng = LatLng(location.latitude, location.longitude)

                    // Add the location to the list for drawing the polyline
                    allLatLng.add(lastLatLng)

                    // Draw a polyline on the map connecting all received locations
                    mMap.addPolyline(
                        PolylineOptions()
                            .color(Color.CYAN)
                            .width(10f)
                            .addAll(allLatLng)
                    )

                    // Include the new location in the bounds for adjusting camera position
                    boundsBuilder.include(lastLatLng)
                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))
                }
            }
        }
    }

    /**
     * Starts receiving location updates.
     *
     * This method requests location updates using the specified location request and callback.
     * It handles any security exceptions that may occur during the process.
     */
    private fun startLocationUpdates() {
        try {
            // Request location updates from the fused location provider client
            fusedLocationClient.requestLocationUpdates(
                locationRequest, // The location request specifying update interval and accuracy
                locationCallback, // The callback to handle location updates
                Looper.getMainLooper() // The looper to execute callbacks on the main thread
            )
        } catch (exception: SecurityException) {
            // Handle security exceptions, such as missing location permissions
            Log.e(TAG, "Error : " + exception.message)
        }
    }

    /**
     * Stops receiving location updates.
     *
     * This method removes the location update request previously initiated by startLocationUpdates().
     * It is called when the location updates are no longer needed or the activity is paused.
     */
    private fun stopLocationUpdates() {
        // Remove location updates using the location callback
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Resumes the activity and restarts location updates if tracking is enabled.
     *
     * This method is called when the activity is resumed. If tracking is enabled, it restarts
     * location updates to continue receiving location data.
     */
    override fun onResume() {
        super.onResume()
        if (isTracking) {
            startLocationUpdates()
        }
    }

    /**
     * Pauses the activity and stops location updates.
     *
     * This method is called when the activity is paused. It stops location updates to conserve
     * battery when the activity is not in the foreground.
     */
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    /**
     * Updates the tracking status and changes the button text accordingly.
     *
     * This method updates the tracking status based on the provided new status. If tracking is
     * enabled, it changes the button text to indicate stopping tracking. If tracking is disabled,
     * it changes the button text to indicate starting tracking.
     *
     * @param newStatus The new tracking status (true if tracking is enabled, false otherwise).
     */
    private fun updateTrackingStatus(newStatus: Boolean) {
        isTracking = newStatus
        if (isTracking) {
            binding.btnStart.text = getString(R.string.stop_running)
        } else {
            binding.btnStart.text = getString(R.string.start_running)
        }
    }

    /**
     * Clears all markers and polylines from the map.
     *
     * This method clears all markers and polylines drawn on the map and resets the list of LatLng
     * coordinates and bounds builder used for drawing polylines and adjusting camera position.
     */
    private fun clearMaps() {
        // Clear all markers and polylines from the map
        mMap.clear()

        // Clear the list of LatLng coordinates and reset the bounds builder
        allLatLng.clear()
        boundsBuilder = LatLngBounds.Builder()
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}