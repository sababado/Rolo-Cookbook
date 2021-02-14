package com.sababado.rolo.cookbook.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.sababado.rolo.cookbook.R
import com.sababado.rolo.cookbook.utils.ext.BasicCallback
import com.sababado.rolo.cookbook.utils.ext.showSnackbar


private const val REQ_CODE = 1122993

fun providePermissionManager(context: Context) = PermissionManager(FirstTimeUsageHandler(context))

class PermissionManager(private val firstTimeUsageHandler: FirstTimeUsageHandler) {

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == PERMISSION_REQUEST_CAMERA) {
//            // Request for camera permission.
//            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission has been granted. Start camera preview Activity.
//                layout.showSnackbar(R.string.camera_permission_granted, Snackbar.LENGTH_SHORT)
//                startCamera()
//            } else {
//                // Permission request was denied.
//                layout.showSnackbar(R.string.camera_permission_denied, Snackbar.LENGTH_SHORT)
//            }
//        }
//    }
//
//    /**
//     * @param snackbarHost Any snackbars will be attached to this view.
//     */
//    fun requestPermission(
//        activity: Activity,
//        permission: String,
//        @StringRes explanationText: Int,
//        snackbarHost: View,
//        onPermissionGranted: BasicCallback
//    ) {
//        requestPermission(
//            activity = activity,
//            permission = permission,
//            listener = object : PermissionAskListener {
//                override fun onNeedPermission() {
//
//                }
//
//                override fun onPermissionPreviouslyDenied() {
//                    // Provide an additional rationale to the user if the permission was not granted
//                    // and the user would benefit from additional context for the use of the permission.
//                    // Display a SnackBar with a button to request the missing permission.
//                    snackbarHost.showSnackbar(
//                        explanationText,
//                        Snackbar.LENGTH_INDEFINITE, R.string.ok
//                    ) {
//                        ActivityCompat.requestPermissions(activity, arrayOf(permission), REQ_CODE)
//                    }
//                }
//
//                override fun onPermissionPreviouslyDeniedWithNeverAskAgain() {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onPermissionGranted() {
//                    onPermissionGranted()
//                }
//            }
//        )
//    }
//
//    fun requestPermission(
//        activity: Activity,
//        permission: String,
//        listener: PermissionAskListener
//    ) {
//        // Check if the Camera permission has been granted
//        if (ActivityCompat.checkSelfPermission(activity, permission)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            // Permission is already available, start camera preview
//            listener.onPermissionGranted()
//        } else {
//            // Permission is missing and must be requested.
//            requestPermission(permission)
//        }
//    }
//
//    /**
//     * Requests the permission.
//     * If an additional rationale should be displayed, the user has to launch the request from
//     * a SnackBar that includes additional information.
//     */
//    private fun requestPermission(activity: Activity, permission: String) {
//        // Permission has not been granted and must be requested.
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
//            // Provide an additional rationale to the user if the permission was not granted
//            // and the user would benefit from additional context for the use of the permission.
//            // Display a SnackBar with a button to request the missing permission.
//            layout.showSnackbar(
//                R.string.camera_access_required,
//                Snackbar.LENGTH_INDEFINITE, R.string.ok
//            ) {
//                ActivityCompat.requestPermissions(activity, arrayOf(permission), REQ_CODE)
//            }
//
//        } else {
//            layout.showSnackbar(R.string.camera_permission_not_available, Snackbar.LENGTH_SHORT)
//
//            // Request the permission. The result will be received in onRequestPermissionResult().
//            requestPermissionsCompat(arrayOf(Manifest.permission.CAMERA), REQ_CODE)
//        }
//    }
//
//    private fun shouldAskPermission(context: Context, permission: String): Boolean {
//        val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
//        if (permissionResult != PackageManager.PERMISSION_GRANTED) {
//            return true
//        }
//        return false
//    }
//
//    /**
//     * Check if the user has permission
//     *
//     * @param activity Current activity
//     * @param permission Permission to check. Look at [Manifest.permission] for good options.
//     * @param listener Permission listener.
//     */
//    fun checkPermission(activity: Activity, permission: String, listener: PermissionAskListener) {
//        if (shouldAskPermission(activity, permission)) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
//                listener.onPermissionPreviouslyDenied()
//            } else {
//                if (firstTimeUsageHandler[permission]) {
//                    listener.onNeedPermission()
//                    firstTimeUsageHandler[permission] = false
//                } else {
//                    listener.onPermissionPreviouslyDeniedWithNeverAskAgain()
//                }
//            }
//        } else {
//            listener.onPermissionGranted()
//        }
//    }
//
//    /**
//     * Listener for checking permissions.
//     */
//    interface PermissionAskListener {
//        /**
//         * Called when permission is needed. First time here.
//         */
//        fun onNeedPermission()
//
//        /**
//         * Called when there's still a chance to get permission. Show some extra rationale.
//         */
//        fun onPermissionPreviouslyDenied()
//
//        /**
//         * Drop it. Stop asking. Don't ask again.
//         */
//        fun onPermissionPreviouslyDeniedWithNeverAskAgain()
//
//        /**
//         * Woohoo! Fun stuff can begin.
//         */
//        fun onPermissionGranted()
//    }
}