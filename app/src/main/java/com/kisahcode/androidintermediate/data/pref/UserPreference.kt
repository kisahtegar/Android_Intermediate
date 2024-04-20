package com.kisahcode.androidintermediate.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extension property to retrieve a DataStore instance for storing preferences associated with the given context.
 * The DataStore instance is lazily created and cached.
 *
 * @property name The name used to identify the DataStore. This is typically used as the name of the preferences file.
 * @return A DataStore instance for storing preferences.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

/**
 * Manages user session data using DataStore for persistent storage.
 *
 * @property dataStore The DataStore instance used for storing user preferences.
 */
class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    /**
     * Saves the user session data to the DataStore.
     *
     * @param user The user model containing session data to be saved.
     */
    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    /**
     * Retrieves the user session data from the DataStore.
     *
     * @return A flow emitting the user model containing session data.
     */
    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    /**
     * Clears the user session data in the DataStore, effectively logging out the user.
     */
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        /**
         * Retrieves the singleton instance of UserPreference.
         *
         * @param dataStore The DataStore instance to be associated with the UserPreference.
         * @return The singleton instance of UserPreference.
         */
        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}