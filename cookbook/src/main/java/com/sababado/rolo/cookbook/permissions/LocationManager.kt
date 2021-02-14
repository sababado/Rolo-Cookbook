package com.sababado.rolo.cookbook.permissions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.sababado.rolo.cookbook.utils.ext.SimpleCallback
import java.lang.ref.WeakReference

private const val REQ_CODE_SETTINGS = 334

/**
 * Add location with this manager!
 *
 * Create an instance of this class then start with [getLastLocation].
 * In the using activity, override [onActivityResult]][Activity.onActivityResult] and call into [LocationManager.onActivityResult].
 *
 * To use this, you'll need to include the Google Play Services dependency in your app.
 * ```gradle
 * implementation 'com.google.android.gms:play-services-location:17.1.0'
 * ```
 *
 * @param appCompatActivity Using activity
 * @param locationCallback Callback to receive location updates.
 */
class LocationManager(
    appCompatActivity: AppCompatActivity,
    val locationCallback: SimpleCallback<Location?>
) {
    private val weakActivity = WeakReference(appCompatActivity)
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Set this before calling [getLastLocation]
     */
    var locationRequest: LocationRequest = defaultLocationRequest()

    /**
     * Default location request.
     * Creates a location request that is balanced between power and accuracy and asks for an update every minute.
     */
    fun defaultLocationRequest() = LocationRequest.create().apply {
        interval = 60 * 1000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }


    /**
     * Start the location getting process! When the location is available, expect a [Location] in the callback.
     * If the settings are not right, this will prompt the user to fix their settings. In which case the result will
     * come back to the using activity's result. Don't forget to pass that into this [onActivityResult].
     *
     * @param locationRequest It is highly recommended to configure and pass in your own location request. One that will suit the purposes of the application best. A [defaultLocationRequest] will be used if you're indecisive.
     */
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        weakActivity.get()?.let { activityRefBeforeLocation ->
            fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(activityRefBeforeLocation)

            // build the settings builder.
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val client: SettingsClient =
                LocationServices.getSettingsClient(activityRefBeforeLocation)

            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            // If the settings are all in order, move on and get the last location
            task.addOnSuccessListener {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        locationCallback(location)
                    }
            }

            // If th settings are not right, there's some work to do.
            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        Log.i("LocationManager", "In settings exception")
                        weakActivity.get()?.let { activityRefAfterLocation ->
                            exception.startResolutionForResult(
                                activityRefAfterLocation,
                                REQ_CODE_SETTINGS
                            )
                        }
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                        Log.i("LocationManager", "In settings exception ignore")
                    }
                }
            }
        }
    }

    /**
     * Try to handle an event that might come back from settings screens.
     * @return True if the event is handled here, false if not.
     */
    fun onActivityResult(requestCode: Int): Boolean {
        Log.i("LocationManager", "In onActivityResult")
        if (requestCode == REQ_CODE_SETTINGS) {
            getLastLocation()
            return true
        }
        return false
    }
}