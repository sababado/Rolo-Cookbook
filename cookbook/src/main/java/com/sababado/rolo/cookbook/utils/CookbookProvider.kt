package com.sababado.rolo.cookbook.utils

import com.sababado.rolo.cookbook.auth.FirebaseUserAccounts
import com.sababado.rolo.cookbook.auth.UserAccounts

/**
 * Get your instances here!
 */
class CookbookProvider {
    /**
     * Returns a new instance of [FirebaseUserAccounts].
     * @return Class conforms to [UserAccounts].
     */
    fun provideFirebaseUserAccounts(): UserAccounts {
        return FirebaseUserAccounts()
    }
}