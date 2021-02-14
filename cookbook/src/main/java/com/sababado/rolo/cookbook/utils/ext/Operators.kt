package com.sababado.rolo.cookbook.utils.ext

import android.content.SharedPreferences
import androidx.core.content.edit

// region SharedPreferences
operator fun SharedPreferences.set(key: String, value: Boolean) = edit { putBoolean(key, value) }
operator fun SharedPreferences.get(key: String?, defaultValue: Boolean): Boolean =
    getBoolean(key, defaultValue)

operator fun SharedPreferences.set(key: String, value: Float) = edit { putFloat(key, value) }
operator fun SharedPreferences.get(key: String?, defaultValue: Float): Float =
    getFloat(key, defaultValue)

operator fun SharedPreferences.set(key: String, value: Int) = edit { putInt(key, value) }
operator fun SharedPreferences.get(key: String?, defaultValue: Int): Int = getInt(key, defaultValue)

operator fun SharedPreferences.set(key: String, value: Long) = edit { putLong(key, value) }
operator fun SharedPreferences.get(key: String?, defaultValue: Long): Long =
    getLong(key, defaultValue)

operator fun SharedPreferences.set(key: String, value: String) = edit { putString(key, value) }
operator fun SharedPreferences.get(key: String?, defaultValue: String): String? =
    getString(key, defaultValue)

operator fun SharedPreferences.set(key: String, value: Set<String>) =
    edit { putStringSet(key, value) }

operator fun SharedPreferences.get(key: String?, defaultValue: Set<String>): MutableSet<String>? =
    getStringSet(key, defaultValue)

// endregion