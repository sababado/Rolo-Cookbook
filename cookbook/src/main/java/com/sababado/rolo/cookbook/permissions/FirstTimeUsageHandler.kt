package com.sababado.rolo.cookbook.permissions

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.sababado.rolo.cookbook.utils.ext.get
import com.sababado.rolo.cookbook.utils.ext.set

/**
 * Function designed specifically to check for first time usage.
 */
class FirstTimeUsageHandler(context: Context, name: String = "first_time") {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE)

    /**
     *
     * Set the value for the permission.
     * @param permission Permission to use
     * @param isFirstTime True if this should register as the first time at next get, false if not.
     */
    fun setFirstTimeAsking(permission: String, isFirstTime: Boolean = false) {
        sharedPreferences[permission] = isFirstTime
    }

    /**
     * Check for a permission. If it doesn't exist, the default is true.
     * @param permission Permission to check.
     */
    fun isFirstTimeAsking(permission: String?): Boolean = sharedPreferences[permission, true]
}

/**
 * Use with `firstTimeUsage[permission]` syntax. If the permission doesn't exist, the default is true.
 */
operator fun FirstTimeUsageHandler.get(permission: String?) = isFirstTimeAsking(permission)

/**
 * Use with `firstTimeUsage[permission] = true` syntax.
 */
operator fun FirstTimeUsageHandler.set(permission: String, isFirstTime: Boolean) = setFirstTimeAsking(permission, isFirstTime)
