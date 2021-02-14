package com.sababado.rolo.cookbook.auth

/**
 * Class to help get user accounts service providers.
 */
@Deprecated(
    message = "Use CookbookProvider instead.",
    replaceWith = ReplaceWith(
        expression = "CookbookProvider",
        imports = ["com.sababado.rolo.cookbook.utils"]
    ),
    level = DeprecationLevel.WARNING
)
object UserAccountsProvider {

    /**
     * Returns a new instance of [FirebaseUserAccounts].
     * @return Class conforms to [UserAccounts].
     */
    fun provideFirebaseUserAccounts(): UserAccounts {
        return FirebaseUserAccounts()
    }
}