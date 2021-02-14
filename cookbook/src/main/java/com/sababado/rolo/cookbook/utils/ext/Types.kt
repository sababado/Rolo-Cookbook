package com.sababado.rolo.cookbook.utils.ext

/**
 * Basic lambda callback.
 * The function should take no parameters and assume no parameters returned.
 */
typealias BasicCallback = () -> Unit

typealias SimpleCallback<T> = (T) -> Unit