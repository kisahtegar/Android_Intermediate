package com.kisahcode.androidintermediate.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kisahcode.androidintermediate.data.UserRepository
import com.kisahcode.androidintermediate.di.Injection
import com.kisahcode.androidintermediate.view.login.LoginViewModel
import com.kisahcode.androidintermediate.view.main.MainViewModel

/**
 * Factory class responsible for creating ViewModel instances.
 *
 * @param repository The UserRepository instance used for interacting with user session data.
 */
class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    /**
     * Creates a new instance of the specified ViewModel class.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A ViewModel instance of the specified class.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        /**
         * Retrieves the singleton instance of ViewModelFactory.
         *
         * @param context The application context used for creating dependencies.
         * @return The singleton instance of ViewModelFactory.
         */
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}