package com.sababado.rolo.cookbook.demo

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.sababado.rolo.cookbook.permissions.LocationManager
import com.sababado.rolo.cookbook.permissions.PermissionManager
import com.sababado.rolo.cookbook.utils.ext.SimpleCallback
import com.sababado.rolo.cookbook.utils.ext.showSnackbar
import com.sababado.rolocookbook.R

/**
 * Needs a permission in the manifest. Either [Manifest.permission.ACCESS_COARSE_LOCATION] or [Manifest.permission.ACCESS_FINE_LOCATION].
 *
 * ```xml
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 * ```
 *
 * This class must implement [ActivityCompat.OnRequestPermissionsResultCallback].
 */
class LocationActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var layout: View
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        layout = findViewById(R.id.main_layout)
        latitudeTextView = findViewById(R.id.latitude_value)
        longitudeTextView = findViewById(R.id.longitude_value)
        locationManager = LocationManager(this) {
            useLocation(it)
        }

        // Register a listener for location button.
        findViewById<Button>(R.id.get_location).setOnClickListener { startLocation() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        PermissionManager.handlePermissionResult(
            requestCode,
            permissions,
            grantResults,
            null,
            permissionGrantedCallback
        )
    }

    // Call back for when the location permission is granted.
    private val permissionGrantedCallback: SimpleCallback<String> = { _ ->
        // Permission has been granted.
        Log.d("LocationActivity", "Permission Granted")
        layout.showSnackbar("Permission Granted", Snackbar.LENGTH_SHORT)
        locationManager.getLastLocation()
    }

    // Start the location work, first by checking for permission.
    private fun startLocation() = PermissionManager.requestPermission(
        appCompatActivity = this,
        permission = Manifest.permission.ACCESS_COARSE_LOCATION,
        snackbarHost = layout,
        rational = R.string.location_rational,
        onPermissionGranted = permissionGrantedCallback
    )

    private fun useLocation(location: Location?) {
        location?.let {
            latitudeTextView.text = location.latitude.toString()
            longitudeTextView.text = location.longitude.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("LocationActivity", "In onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        locationManager.onActivityResult(requestCode)
    }
}