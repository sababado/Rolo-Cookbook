package com.sababado.rolo.cookbook.demo

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        layout = findViewById(R.id.main_layout)

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
        layout.showSnackbar("Permission Granted", Snackbar.LENGTH_SHORT)
        getCurrentLocation()
    }

    // Start the location work, first by checking for permission.
    private fun startLocation() = PermissionManager.requestPermission(
        appCompatActivity = this,
        permission = Manifest.permission.ACCESS_COARSE_LOCATION,
        snackbarHost = layout,
        rational = R.string.location_rational,
        onPermissionGranted = permissionGrantedCallback
    )

    // Called when location permission is available and we can do some work!
    private fun getCurrentLocation() {

    }
}