package com.sababado.rolo.cookbook.permissions

import android.content.pm.PackageManager
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.sababado.rolo.cookbook.R
import com.sababado.rolo.cookbook.permissions.PermissionManager.handlePermissionResult
import com.sababado.rolo.cookbook.permissions.PermissionManager.isPermissionAvailable
import com.sababado.rolo.cookbook.permissions.PermissionManager.requestPermission
import com.sababado.rolo.cookbook.utils.ext.*


private const val REQ_CODE = 333

/**
 * 1. The using activity should implement [ActivityCompat.OnRequestPermissionsResultCallback] and override [onRequestPermissionsResult][ActivityCompat.OnRequestPermissionsResultCallback.onRequestPermissionsResult].
 * 2. Start the request with one of the [requestPermission] functions. One simplifies the process with a snackbar, the other is flexible for more detailed usage.
 * 3. Handle the results in [handlePermissionResult].
 *
 * If permissions are denied, the only callback will be in the [handlePermissionResult] function.
 *
 * Check for one off permissions with [isPermissionAvailable].
 */
object PermissionManager {

    /**
     * Requests the permission with a convenience [Snackbar].
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     * @param permission The permission to request.
     * @param snackbarHost The layout that the snackbar should attach to.
     * @param rational The message that should be shown to explain the permission.
     */
    @JvmStatic
    fun requestPermission(
        appCompatActivity: AppCompatActivity,
        permission: String,
        snackbarHost: View,
        @StringRes rational: Int,
        onPermissionGranted: SimpleCallback<String>,
    ) {
        // Permission has not been granted and must be requested.
        requestPermission(appCompatActivity, permission, onPermissionGranted) { cont ->
            snackbarHost.showSnackbar(rational, Snackbar.LENGTH_INDEFINITE, R.string.ok) {
                cont()
            }
        }
    }

    /**
     * Requests the permission.
     * If an additional rationale should be displayed, [onShowRational] will be invoked which has a [BasicCallback]
     * parameter. `onShowRational` should show some message to the user requiring manual input to continue or not.
     * If the user decides to continue, invoke the `BasicCallback` parameter.
     * @param permission The permission to request.
     * @param onShowRational The rational callback. If continuance is required, invoke the callback.
     */
    @JvmStatic
    fun requestPermission(
        appCompatActivity: AppCompatActivity,
        permission: String,
        onPermissionGranted: SimpleCallback<String>,
        onShowRational: SimpleCallback<BasicCallback>,
    ) {
        if (appCompatActivity.checkSelfPermissionCompat(permission) == PackageManager.PERMISSION_GRANTED) {
            // Easy. Already done.
            onPermissionGranted(permission)
        } else {
            // Permission has not been granted and must be requested.
            if (appCompatActivity.shouldShowRequestPermissionRationaleCompat(permission)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // Display a SnackBar with a button to request the missing permission.
                onShowRational {
                    appCompatActivity.requestPermissionsCompat(arrayOf(permission), REQ_CODE)
                }
            } else {
                // Request the permission. The result will be received in onRequestPermissionResult().
                appCompatActivity.requestPermissionsCompat(arrayOf(permission), REQ_CODE)
            }
        }
    }

    /**
     * See if the permission is already available or not.
     * @param appCompatActivity Activity to use
     * @param permission The permission to check.
     * @return True if the permission is available, false if not.
     */
    @JvmStatic
    fun isPermissionAvailable(appCompatActivity: AppCompatActivity, permission: String): Boolean {
        return appCompatActivity.checkSelfPermissionCompat(permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * When [onRequestPermissionsResult][ActivityCompat.OnRequestPermissionsResultCallback.onRequestPermissionsResult] is called,
     * send it all to this function to handle the results. This supports multiple permission requests and
     * [onPermissionGranted] will be called for each one of them. [onPermissionDenied] will be called only if passed in.
     * @param requestCode Request code used
     * @param permissions Permissions requested
     * @param grantResults Permission results
     * @param onPermissionDenied Optional. Called for every permission denied.
     * @param onPermissionGranted Called for every permission granted.
     */
    fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        onPermissionDenied: SimpleCallback<String>? = null,
        onPermissionGranted: SimpleCallback<String>
    ) {
        if (requestCode == REQ_CODE) {
            permissions.forEachIndexed { i, permission ->
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted(permission)
                } else {
                    onPermissionDenied?.let { it(permission) }
                }
            }
        }
    }
}