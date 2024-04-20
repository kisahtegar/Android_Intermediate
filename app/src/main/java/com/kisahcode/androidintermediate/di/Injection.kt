package com.kisahcode.androidintermediate.di

import android.content.Context
import com.kisahcode.androidintermediate.data.UserRepository
import com.kisahcode.androidintermediate.data.pref.UserPreference
import com.kisahcode.androidintermediate.data.pref.dataStore

/**
 * Dependency injection object responsible for providing instances of repositories and other dependencies.
 */
object Injection {

    /**
     * Provides a singleton instance of UserRepository.
     *
     * @param context The application context used for creating dependencies.
     * @return An instance of UserRepository.
     */
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}