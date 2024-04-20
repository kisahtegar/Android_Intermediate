package com.kisahcode.androidintermediate.data

import com.kisahcode.androidintermediate.data.pref.UserModel
import com.kisahcode.androidintermediate.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

/**
 * Repository responsible for managing user session data.
 *
 * @property userPreference The UserPreference instance used for interacting with user session data.
 */
class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    /**
     * Saves the user session data.
     *
     * @param user The UserModel containing session data to be saved.
     */
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    /**
     * Retrieves the user session data.
     *
     * @return A flow emitting the UserModel containing session data.
     */
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    /**
     * Logs out the user by clearing session data.
     */
    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        /**
         * Retrieves the singleton instance of UserRepository.
         *
         * @param userPreference The UserPreference instance to be associated with the UserRepository.
         * @return The singleton instance of UserRepository.
         */
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}