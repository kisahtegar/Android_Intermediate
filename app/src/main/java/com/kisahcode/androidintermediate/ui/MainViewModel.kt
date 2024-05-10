package com.kisahcode.androidintermediate.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kisahcode.androidintermediate.data.QuoteRepository
import com.kisahcode.androidintermediate.di.Injection
import com.kisahcode.androidintermediate.network.QuoteResponseItem

/**
 * ViewModel class responsible for providing quote-related data to the UI.
 *
 * @param quoteRepository Repository for accessing quote-related data.
 */
class MainViewModel(quoteRepository: QuoteRepository) : ViewModel() {

    /**
     * LiveData object containing a stream of paged quote data.
     */
    val quote: LiveData<PagingData<QuoteResponseItem>> =
        quoteRepository.getQuote().cachedIn(viewModelScope)

}

/**
 * ViewModelProvider.Factory implementation for creating MainViewModel instances with a provided Context.
 *
 * @property context Application context.
 */
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}