package com.sababado.rolo.cookbook

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import com.sababado.rolo.cookbook.NetworkConnection.Companion.appearOffline
import com.sababado.rolo.cookbook.NetworkConnection.Companion.isNetworkConnected
import com.sababado.rolo.cookbook.NetworkConnection.Companion.shouldAppearOnline


/**
 * To use this, don't forget to add the permission.
 *
 * <code><uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /></code
 *
 * Use [registerNetworkCallback(context)][registerNetworkCallback] to check for a network connection and receive updates for when the connection drops, or comes back.
 * Check the network state with [isNetworkConnected]. Set a flag to appear offline with [appearOffline]. Get the combined state with [shouldAppearOnline]
 */

class NetworkConnection(context: Context) {

    companion object {
        /**
         * Set flag to true to force [isNetworkConnected] to always return false.
         */
        var appearOffline = false;

        /**
         * Check if the network is connected or not. If [appearOffline] is true, this will always return false.
         */
        var isNetworkConnected = true

        /**
         * Combination of [appearOffline] and [isNetworkConnected].
         */
        fun shouldAppearOnline(): Boolean {
            return !appearOffline && isNetworkConnected
        }
    }

    init {
        registerNetworkCallback(context)
    }

    @SuppressLint("MissingPermission")
    private fun registerNetworkCallback(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : NetworkCallback() {

            override fun onAvailable(network: Network) {
                isNetworkConnected = true // Global Static Variable
            }

            override fun onLost(network: Network) {
                isNetworkConnected = false // Global Static Variable
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
        isNetworkConnected = false
    }
}