package com.sababado.rolo.cookbook.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.annotation.IdRes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.sababado.rolo.cookbook.utils.MetaDataUtils
import java.lang.ref.WeakReference


private const val REQ_CODE_GOOGLE_SIGN_IN = 111
private val TAG = GoogleSignInHelper::class.java.name

/**
 * Adds Google Sign in anywhere!
 *
 * Step 1. Add the [GoogleSignInHelperCallbacks] for basic state handling. For example:
 * ```
 * override fun onLoginSuccess(wasAlreadySignedIn: Boolean, refreshRequired: Boolean) {
 *     sign_in_layout.visibility = View.GONE
 *
 *     if (refreshRequired) {
 *         viewModel.updateData()
 *     }
 * }
 *
 * override fun onLoginFail() {
 *     Snackbar.make(requireView(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
 * }
 * ```
 *
 * Step 2. Initialize the helper.
 *
 * ```
 * private val googleSignInHelper: GoogleSignInHelper by lazy {
 *     GoogleSignInHelper(
 *         this,
 *         (requireActivity().application as MyApp).factory.provideUserAccounts(),
 *         requireActivity()
 *     )
 * }
 * ```
 *
 * Step 3. Add the lifecycle hooks
 *
 * - `onActivityCreated`: in a fragment should be in `onActivityCreated`, in an Activity this should be in `onCreate`.
 * - `onActivityStart`
 * - `onActivityResult`
 *
 */
class GoogleSignInHelper(
    private val callbacks: GoogleSignInHelperCallbacks,
    private val userAccounts: UserAccounts,
    activity: Activity
) {
    private val weakActivity: WeakReference<Activity> = WeakReference(activity)

    private var googleSignInClient: GoogleSignInClient? = null

    fun onActivityCreated(@IdRes googleSignInButtonIdRes: Int) {
        weakActivity.get()?.let { activity ->
            val webClientId: String =
                MetaDataUtils.requireMetaDataFlag(activity, "rolo.cookbook.google_web_client_id")
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(activity, gso)

            activity.findViewById<SignInButton>(googleSignInButtonIdRes)
                .setOnClickListener { signIn() }
        }
    }

    fun onActivityStart() {
        weakActivity.get()?.let { activity ->
            val account = GoogleSignIn.getLastSignedInAccount(activity)
            if (account != null) {
                callbacks.onLoginSuccess(
                    wasAlreadySignedIn = true,
                    refreshRequired = false,
                    isFirstLogin = false
                )
            }
        }
    }

    private fun signIn() {
        googleSignInClient?.let {
            weakActivity.get()?.startActivityForResult(it.signInIntent, REQ_CODE_GOOGLE_SIGN_IN)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        weakActivity.get()?.let { activity ->
            if (requestCode == REQ_CODE_GOOGLE_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    userAccounts.linkWithGoogle(activity, account.idToken!!, callbacks)
                } catch (e: ApiException) {
                    // The ApiException status code indicates the detailed failure reason.
                    // Please refer to the GoogleSignInStatusCodes class reference for more information.

                    val message = "signin: signInResult:failed code=${e.statusCode}; ${e.message}"
                    Log.w(TAG, message)
                    callbacks.onLoginFail(e, message)
                }
            }
        }
    }
}

interface GoogleSignInHelperCallbacks {
    /**
     * @param wasAlreadySignedIn True if the user was already signed in, no manual interaction needed.
     * @param refreshRequired True if the user's account totally changed. Do a refresh and get the new user's data.
     * @param isFirstLogin True if the user has Signed Up, false if this is a login.
     */
    fun onLoginSuccess(wasAlreadySignedIn: Boolean, refreshRequired: Boolean, isFirstLogin: Boolean)
    fun onLoginFail(exception: Exception, customLog: String)
}