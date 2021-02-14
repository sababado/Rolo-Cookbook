package com.sababado.rolocookbook.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.sababado.rolo.cookbook.utils.ext.checkSelfPermissionCompat
import com.sababado.rolo.cookbook.utils.ext.requestPermissionsCompat
import com.sababado.rolo.cookbook.utils.ext.shouldShowRequestPermissionRationaleCompat
import com.sababado.rolo.cookbook.utils.ext.showSnackbar
import com.sababado.rolocookbook.R

private const val REQ_CODE = 1122993

/**
 * Needs a permission in the manifest. Either [Manifest.permission.ACCESS_COARSE_LOCATION] or [Manifest.permission.ACCESS_FINE_LOCATION].
 *
 * ```xml
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 * ```
 */
class LocationActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        layout = findViewById(R.id.main_layout)

        // Register a listener for the 'Show Camera Preview' button.
        findViewById<Button>(R.id.get_location).setOnClickListener { startPermissionRequest(Manifest.permission.ACCESS_COARSE_LOCATION) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQ_CODE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                layout.showSnackbar("Permission Granted", Snackbar.LENGTH_SHORT)
                getCurrentLocation()
            } else {
                // Permission request was denied.
                layout.showSnackbar("Permission Denied", Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun startPermissionRequest(permission: String) {
        // Check if the Camera permission has been granted
        if (checkSelfPermissionCompat(permission) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            layout.showSnackbar("Permission Available", Snackbar.LENGTH_SHORT)
            getCurrentLocation()
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission(permission)
        }
    }

    /**
     * Requests the permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestCameraPermission(permission: String) {
        // Permission has not been granted and must be requested.
        if (shouldShowRequestPermissionRationaleCompat(permission)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            layout.showSnackbar(
                "Permission Required",
                Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                requestPermissionsCompat(arrayOf(permission), REQ_CODE)
            }

        } else {
            layout.showSnackbar("Permission Not Available", Snackbar.LENGTH_SHORT)

            // Request the permission. The result will be received in onRequestPermissionResult().
            requestPermissionsCompat(arrayOf(permission), REQ_CODE)
        }
    }

    private fun getCurrentLocation() {

    }
}