package com.kisahcode.androidintermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.kisahcode.androidintermediate.database.QuoteDatabase
import com.kisahcode.androidintermediate.network.ApiService
import com.kisahcode.androidintermediate.network.QuoteResponseItem

/**
 * Repository class responsible for managing quote data.
 *
 * This class serves as an intermediary between the UI and data sources, providing methods
 * for accessing quote data from the network or local database and exposing it to the UI as
 * LiveData streams of paginated data.
 *
 * @property quoteDatabase The QuoteDatabase instance for accessing local quote data.
 * @property apiService The ApiService instance for making network requests.
 */
class QuoteRepository(private val quoteDatabase: QuoteDatabase, private val apiService: ApiService) {

    /**
     * Retrieves paginated quote data from the network.
     *
     * This function sets up a Pager with the specified configuration and paging source factory,
     * then converts the resulting PagingData stream into a LiveData object for observation by the UI.
     *
     * @return LiveData object containing paginated quote data.
     */
    fun getQuote(): LiveData<PagingData<QuoteResponseItem>> {
        // Initialize a Pager with the specified configuration and paging source factory
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            // Use QuoteRemoteMediator as the remote mediator for loading data from the network
            remoteMediator = QuoteRemoteMediator(quoteDatabase, apiService),
            // Use QuoteDao's getAllQuote() method as the local paging source
            pagingSourceFactory = {
                quoteDatabase.quoteDao().getAllQuote()
            }
        ).liveData
    }
}