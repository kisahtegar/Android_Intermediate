package com.kisahcode.androidintermediate

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kisahcode.androidintermediate.databinding.ActivityMapsBinding

/**
 * This activity displays a Google Map and shows the user's last known location on the map.
 * It also handles location permission requests and updates.
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        getMyLastLocation()
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
}