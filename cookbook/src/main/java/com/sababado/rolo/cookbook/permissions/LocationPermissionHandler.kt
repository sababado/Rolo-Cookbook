package com.sababado.rolo.cookbook.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sababado.rolo.cookbook.utils.ext.BasicCallback
import java.lang.ref.WeakReference

/**
 * Helper class to request permissions from the user for location.
 * Location is *NOT* required for this app to function, only helps center the map on the user's location for convenience.
 *
 * Using classes must define the [onSuccess].
 *
 * This can be used as a quick builder or standalone for detailed configuration.
 *
 * ```kotlin
 * // builder pattern
 * enableLocation(this) {
 *    requiresFineLocation = false
 *    customExplanationMessage = getString(R.string.custom_location_message)
 *    onSuccess = {}
 *    onDenied = {}
 *    tryEnablingLocation()
 * }
 *
 * // direct access if more fine tuning is needed.
 * val handler = LocationPermissionHandler(this)
 * handler.requiresFineLocation = false
 * handler.customExplanationMessage = getString(R.string.custom_location_message)
 * handler.onSuccess = {}
 * handler.onDenied = {}
 * handler.tryEnablingLocation()
 * ```
 *
 * COOKBOOK ITEM
 *
 * @param activity The activity that this handler should be tied to. The reference will be weak.
 */
class LocationPermissionHandler(activity: Activity) {
    private val weakActivity = WeakReference(activity)

    /**
     * Set to true to look for [Manifest.permission.ACCESS_FINE_LOCATION]. Needed for BLE connection.
     * Default value is false and the handler will check for [Manifest.permission.ACCESS_COARSE_LOCATION], which is accurate to a city block.
     */
    var requiresFineLocation: Boolean = false
        set(value) {
            field = value
            permissionType = if (requiresFineLocation) Manifest.permission.ACCESS_FINE_LOCATION else Manifest.permission.ACCESS_COARSE_LOCATION
        }

    private var permissionType = Manifest.permission.ACCESS_COARSE_LOCATION

    /**
     * Request code for location permission request.
     *
     * @see [onRequestPermissionsResult]
     */
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * [.onRequestPermissionsResult].
     */
    private var permissionDenied = false

    /**
     * Set this message if a custom message should be shown to the user.
     */
    var customExplanationMessage: String? = null

    lateinit var onSuccess: BasicCallback

    /**
     * Handle the case if the user denies permission to the permission.
     */
    var onDenied: BasicCallback? = null


    /**
     * Try enabling the user's location. If permission asks are required, this will happen.
     */
    fun tryEnablingLocation() {
        weakActivity.get()?.let {
            if (ContextCompat.checkSelfPermission(it, permissionType) == PackageManager.PERMISSION_GRANTED) {
                onSuccess()
            } else {
                // Permission to access the location is missing. Show rationale and request permission
//                PermissionManager.requestPermission(it, LOCATION_PERMISSION_REQUEST_CODE, permissionType, true);
            }
        }
    }
}

/**
 * Configure a [LocationPermissionHandler] as a builder.
 * Be sure to call [onSuccess][LocationPermissionHandler.onSuccess] and finish the block with [tryEnablingLocation][LocationPermissionHandler.tryEnablingLocation].
 *
 * ```kotlin
 * // builder pattern
 * enableLocation(this) {
 *    requiresFineLocation = false
 *    customExplanationMessage = getString(R.string.custom_location_message)
 *    onSuccess = {}
 *    onDenied = {}
 *    tryEnablingLocation()
 * }
 */
fun enableLocation(activity: Activity, init: LocationPermissionHandler.() -> Unit): LocationPermissionHandler {
    val handler = LocationPermissionHandler(activity)
    handler.init()
    return handler
}