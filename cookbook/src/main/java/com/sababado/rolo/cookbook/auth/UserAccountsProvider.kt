package com.sababado.rolo.cookbook.auth

/**
 * Class to help get user accounts service providers.
 */
object UserAccountsProvider {

    /**
     * Returns a new instance of [FirebaseUserAccounts].
     * @return Class conforms to [UserAccounts].
     */
    fun provideFirebaseUserAccounts(): UserAccounts {
        return FirebaseUserAccounts()
    }
}