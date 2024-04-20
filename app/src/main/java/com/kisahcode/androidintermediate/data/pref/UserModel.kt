package com.kisahcode.androidintermediate.data.pref

/**
 * Represents a user model containing user information.
 *
 * @property email The email address of the user.
 * @property token The authentication token associated with the user.
 * @property isLogin A flag indicating whether the user is currently logged in. Defaults to `false`.
 */
data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)