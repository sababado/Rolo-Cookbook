package com.sababado.rolo.cookbook.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.*

internal class FirebaseUserAccounts : UserAccounts {
    companion object {
        private val TAG = FirebaseUserAccounts::class.java.name
    }

    override fun requireCurrentUserId(): String {
        return requireCurrentUser().uid
    }

    override fun currentUserExists(): Boolean = getCurrentUser() != null

    /**
     * Link the current user to a google account
     */
    override fun linkWithGoogle(
        activity: Activity,
        idToken: String,
        callbacks: GoogleSignInHelperCallbacks
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        requireCurrentUser().linkWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    callbacks.onLoginSuccess(
                        wasAlreadySignedIn = false,
                        refreshRequired = false,
                        isFirstLogin = true
                    )
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        (task.exception as FirebaseAuthUserCollisionException).updatedCredential?.let { newAuth ->
                            linkAndMerge(newAuth, callbacks)
                        }
                    } else {
                        val msg = "signin: linkWithCredential:failure"
                        val e = task.exception ?: Exception("Unknown exception")
                        Log.w("UserAccounts", msg, e)
                        callbacks.onLoginFail(e, msg)
                    }
                }
            }
    }

    private fun linkAndMerge(credential: AuthCredential, callbacks: GoogleSignInHelperCallbacks) {
//        Log.w("UserAccounts", "signin: prevUserId2: ${prevUser.uid}")
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnSuccessListener { result ->
//                val currentUser = result.user
//                Log.w("UserAccounts", "signin: prevUserId3: ${prevUser.uid}")
//                Log.w("UserAccounts", "signin: currentUserId: ${currentUser!!.uid}")
//                deleteUser(application, prevUser)
                callbacks.onLoginSuccess(
                    wasAlreadySignedIn = false,
                    refreshRequired = true,
                    isFirstLogin = false
                )
            }
            .addOnFailureListener {
                val msg = "signin: signInWith updated credentials :failure"
                Log.w("UserAccounts", msg, it)
                callbacks.onLoginFail(it, msg)
            }
    }

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    private fun requireCurrentUser(): FirebaseUser {
        return getCurrentUser()!!
    }

//    private fun deleteUser(application: Application, user: FirebaseUser?) {
//        user?.let {
////            Log.w("UserAccounts", "signin: deleting: ${it.uid}")
//            GlobalScope.launch(Dispatchers.IO) {
//                (application as MyApp).factory.provideApi().deleteUserData(it.uid)
//                it.delete()
//            }
//        }
//    }

    /**
     * Quick option for anonymous sign in.
     */
    override fun signInAnonymously(
        activity: Activity,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        auth.signInAnonymously()
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success: ${user?.uid} / ${user?.displayName}")
                    onSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    onFailure()
                }
            }
    }
}