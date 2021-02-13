package com.sababado.rolo.cookbook.auth

import android.app.Activity

/**
 * Interface for getting user account data.
 */
interface UserAccounts {
    /**
     * Get the current user's id.
     * @return String representation of the Id.
     */
    fun requireCurrentUserId(): String

    /**
     * Check if a current user is logged in.
     * @return True for yes, False for no.
     */
    fun currentUserExists(): Boolean

    /**
     * Link the current user to a google account
     */
    fun linkWithGoogle(activity: Activity, idToken: String, callbacks: GoogleSignInHelperCallbacks)

    /**
     * Quick option for anonymous sign in.
     * @param activity The current activity to use.
     * @param onSuccess Code block called if the operation is successful.
     * @param onFailure Code block called if the operation fails.
     */
    fun signInAnonymously(activity: Activity, onSuccess: () -> Unit, onFailure: () -> Unit)
}