package com.kisahcode.androidintermediate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kisahcode.androidintermediate.data.UploadRepository
import com.kisahcode.androidintermediate.data.di.Injection

/**
 * Factory class responsible for creating instances of ViewModels with the required dependencies.
 *
 * This factory provides a centralized way to create ViewModels, ensuring they are constructed
 * with the appropriate dependencies.
 *
 * @property repository The repository providing data operations for ViewModels.
 * @constructor Creates an instance of ViewModelFactory with the specified repository.
 */

class ViewModelFactory(private val repository: UploadRepository) :
    ViewModelProvider.NewInstanceFactory() {

    /**
     * Creates a new instance of the specified ViewModel class with the required dependencies.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return An instance of the requested ViewModel class.
     * @throws IllegalArgumentException If the ViewModel class is unknown or unsupported.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        /**
         * Retrieves the singleton instance of the ViewModelFactory.
         *
         * @return The singleton instance of ViewModelFactory.
         */
        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }.also { instance = it }
    }
}