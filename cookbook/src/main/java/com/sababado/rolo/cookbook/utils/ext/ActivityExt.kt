package com.sababado.rolo.cookbook.utils.ext

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Quick start activity.
 *
 * Simple usage
 * ```kotlin
 * startActivity<MainActivity>()
 * ```
 *
 * Usage with intent configuration
 * ```kotlin
 * startActivity<MainActivity>{
 *     putExtra("param 1", "Simple")
 * }
 * ```
 */
inline fun <reified T : Activity> Context.startActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}