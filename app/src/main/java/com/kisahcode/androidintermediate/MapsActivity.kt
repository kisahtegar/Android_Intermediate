package com.kisahcode.androidintermediate

import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.kisahcode.androidintermediate.databinding.ActivityMapsBinding

/**
 * This activity displays a Google Map with various functionalities such as adding markers,
 * changing map types, and handling user interactions.
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Callback method invoked when the Google Map is ready for use.
     *
     * This method is called when the Google Map is initialized and ready for interaction. It sets
     * up the map with initial settings, such as adding markers, setting up listeners, and adjusting
     * the camera position. If Google Play services is not installed on the device, the user will be
     * prompted to install it inside the SupportMapFragment. This method will only be triggered once
     * the user has installed Google Play services and returned to the app.
     *
     * @param googleMap The GoogleMap instance representing the map that is ready for use.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        // Assign the GoogleMap instance to the local mMap variable
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Add a marker for Dicoding Space and move the camera to its location
        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
        mMap.addMarker(
            MarkerOptions()
                .position(dicodingSpace)
                .title("Dicoding Space")
                .snippet("Batik Kumeli No.50")
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))

        // Set up a listener to add a new marker when the map is long-clicked
        mMap.setOnMapLongClickListener { latLng ->
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("New Marker")
                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                    .icon(vectorToBitmap(R.drawable.ic_android, Color.parseColor("#3DDC84")))
            )
        }

        // Set up a listener to display a marker's info window when a Point of Interest (POI) is clicked
        mMap.setOnPoiClickListener { pointOfInterest ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(pointOfInterest.latLng)
                    .title(pointOfInterest.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
            poiMarker?.showInfoWindow()
        }

        // retrieves the user's current location.
        getMyLocation()

        // Sets custom map style to the Google Map.
        setMapStyle()

        addManyMarker()
    }

    /**
     * Initializes the options menu for the map activity.
     *
     * This method inflates the menu resource file (map_options.xml) to create the options menu
     * for the map activity. The menu items include different map types such as Normal, Satellite,
     * Terrain, and Hybrid. These options allow the user to change the map view type.
     *
     * @param menu The menu to initialize.
     * @return Boolean value indicating whether the menu creation was successful.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    /**
     * Handles selection of items in the options menu.
     *
     * This method is called when a menu item in the options menu is selected by the user.
     * It determines which menu item was selected and performs the corresponding action.
     * The available actions include changing the map view type to Normal, Satellite, Terrain,
     * or Hybrid.
     *
     * @param item The menu item that was selected.
     * @return Boolean value indicating whether the selection was handled successfully.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                // Change the map view type to Normal
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                // Change the map view type to Satellite
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                // Change the map view type to Terrain
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                // Change the map view type to Hybrid
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                // If the selected menu item is not recognized, call the superclass implementation
                super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * Converts a vector drawable to a BitmapDescriptor with specified color.
     *
     * This method takes a vector drawable resource ID and a color, and converts the vector
     * drawable into a BitmapDescriptor, which can be used as a marker icon on the Google Map.
     * It allows customization of the marker icon color.
     *
     * @param id The resource id of the vector drawable.
     * @param color The color to apply to the vector drawable.
     * @return The BitmapDescriptor representing the vector drawable with specified color.
     */
    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        // Retrieve the vector drawable from resources using its resource ID
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)

        // Check if the vector drawable is found in resources
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            // If not found, return default marker icon
            return BitmapDescriptorFactory.defaultMarker()
        }

        // Create a bitmap with dimensions matching the intrinsic dimensions of the vector drawable
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // Create a canvas to draw the vector drawable onto the bitmap
        val canvas = Canvas(bitmap)

        // Set the bounds of the vector drawable to the bounds of the canvas
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)

        // Apply the specified color to the vector drawable
        DrawableCompat.setTint(vectorDrawable, color)

        // Draw the vector drawable onto the canvas
        vectorDrawable.draw(canvas)

        // Convert the bitmap to a BitmapDescriptor and return it
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    /**
     * Activity result launcher to handle location permission requests.
     *
     * This launcher registers an activity result callback for requesting location permissions.
     * When the result is received, it invokes the [getMyLocation] method if the permission is granted.
     */
    private val requestPermissionLauncher =
        // If location permission is granted, call the getMyLocation method
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    /**
     * Requests and retrieves the user's current location.
     *
     * This method checks if the ACCESS_FINE_LOCATION permission is granted.
     * If the permission is granted, it enables the My Location feature on the Google Map.
     * If not, it launches a permission request using the requestPermissionLauncher.
     */
    private fun getMyLocation() {
        // Check if the ACCESS_FINE_LOCATION permission is granted
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is granted, enable My Location on the Google Map
            mMap.isMyLocationEnabled = true
        } else {
            // If permission is not granted, launch permission request using requestPermissionLauncher
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Sets custom map style to the Google Map.
     *
     * This method attempts to set a custom map style defined in a JSON file resource. The map style
     * resource file should be located in the res/raw directory. If the style parsing is successful,
     * it applies the custom map style to the Google Map. If parsing fails or the style resource is
     * not found, appropriate error messages are logged.
     *
     * For more information and customization of map styles, visit:
     * [https://mapstyle.withgoogle.com]
     */
    private fun setMapStyle() {
        try {
            // Attempt to set the custom map style from the raw resource file
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            )

            // Check if style parsing was successful
            if (!success) {
                // Log an error message if style parsing failed
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            // Log an error message if the style resource file is not found
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    /**
     * Adds multiple markers for tourism places to the Google Map.
     *
     * This method adds multiple markers to the Google Map for each tourism place provided in the list.
     * It iterates over the list of tourism places, creates a marker for each place with its name
     * and coordinates, and includes the marker's position in the LatLngBounds Builder to adjust the
     * camera position and zoom level to fit all markers within the viewport.
     */
    private fun addManyMarker() {
        // List of tourism places with their names and coordinates
        val tourismPlace = listOf(
            TourismPlace("Floating Market Lembang", -6.8168954,107.6151046),
            TourismPlace("The Great Asia Africa", -6.8331128,107.6048483),
            TourismPlace("Rabbit Town", -6.8668408,107.608081),
            TourismPlace("Alun-Alun Kota Bandung", -6.9218518,107.6025294),
            TourismPlace("Orchid Forest Cikole", -6.780725, 107.637409),
        )

        // Iterate over the list of tourism places
        tourismPlace.forEach { tourism ->
            // Create a LatLng object representing the location of the tourism place
            val latLng = LatLng(tourism.latitude, tourism.longitude)
            // Add a marker to the map for the tourism place with its name
            mMap.addMarker(MarkerOptions().position(latLng).title(tourism.name))
            // Include the marker's position in the LatLngBounds Builder to adjust camera position
            boundsBuilder.include(latLng)
        }

        // Build the LatLngBounds from the boundsBuilder
        val bounds: LatLngBounds = boundsBuilder.build()

        // Animate the camera to fit the entire bounds within the viewport with padding
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    companion object {
        private val TAG = MapsActivity::class.java.simpleName
    }
}

/**
 * Data class representing a tourism place with name, latitude, and longitude.
 *
 * This data class encapsulates information about a tourism place including its name,
 * latitude, and longitude coordinates.
 *
 * @property name The name of the tourism place.
 * @property latitude The latitude coordinate of the tourism place.
 * @property longitude The longitude coordinate of the tourism place.
 */
data class TourismPlace(
    val name: String,
    val latitude: Double,
    val longitude: Double
)