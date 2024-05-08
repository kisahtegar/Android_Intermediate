package com.kisahcode.androidintermediate.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kisahcode.androidintermediate.data.NewsRepository
import com.kisahcode.androidintermediate.di.Injection
import com.kisahcode.androidintermediate.ui.detail.NewsDetailViewModel
import com.kisahcode.androidintermediate.ui.list.NewsViewModel

/**
 * ViewModelFactory class for creating ViewModel instances with dependencies.
 *
 * This factory class provides ViewModel instances with dependencies such as the NewsRepository.
 * It implements the ViewModelProvider.Factory interface to create ViewModel instances for the
 * NewsViewModel and NewsDetailViewModel classes.
 */
class ViewModelFactory private constructor(private val newsRepository: NewsRepository) :
    ViewModelProvider.NewInstanceFactory() {

    /**
     * Creates a new instance of the specified ViewModel class with the provided dependencies.
     *
     * This method checks the specified ViewModel class and creates a new instance
     * with the appropriate dependencies from the NewsRepository.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return A new instance of the specified ViewModel class.
     * @throws IllegalArgumentException if the ViewModel class is unknown.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsRepository) as T
        } else if (modelClass.isAssignableFrom(NewsDetailViewModel::class.java)) {
            return NewsDetailViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    /**
     * Companion object to provide a singleton instance of ViewModelFactory.
     */
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        /**
         * Gets a singleton instance of ViewModelFactory with the provided context.
         *
         * This method ensures that only one instance of ViewModelFactory is created,
         * using lazy initialization to provide thread safety.
         *
         * @param context The context used to provide the NewsRepository dependency.
         * @return A singleton instance of ViewModelFactory.
         */
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}