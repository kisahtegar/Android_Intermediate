package com.kisahcode.androidintermediate.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kisahcode.androidintermediate.database.QuoteDatabase
import com.kisahcode.androidintermediate.network.ApiService
import com.kisahcode.androidintermediate.network.QuoteResponseItem

/**
 * Remote mediator responsible for coordinating data loading between the remote API and local database.
 *
 * This class implements the RemoteMediator interface provided by Jetpack Paging 3. It handles loading
 * paginated data from a remote API, inserting it into a local Room database, and providing the loaded data
 * to the UI through PagingData.
 *
 * @param database The Room database instance for storing quote data locally.
 * @param apiService The Retrofit API service interface for fetching quote data from the remote server.
 */
@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
   private val database: QuoteDatabase,
   private val apiService: ApiService
) : RemoteMediator<Int, QuoteResponseItem>() {

   private companion object {
      /** The initial page index to start loading data from. */
      const val INITIAL_PAGE_INDEX = 1
   }

   /**
    * Initializes the remote mediator.
    *
    * This method is called when the remote mediator is initialized. It returns InitializeAction.LAUNCH_INITIAL_REFRESH
    * to trigger a refresh of the data when the app starts.
    */
   override suspend fun initialize(): InitializeAction {
      return InitializeAction.LAUNCH_INITIAL_REFRESH
   }

   /**
    * Loads data from the remote API and inserts it into the local database.
    *
    * This method is responsible for loading data based on the LoadType provided. It queries the API
    * for quote data, inserts it into the local database, and returns a MediatorResult indicating
    * success or failure.
    *
    * @param loadType The type of data load requested (REFRESH, PREPEND, or APPEND).
    * @param state The current PagingState, including information about the currently loaded data.
    * @return A MediatorResult indicating success or failure, along with information about the end of pagination.
    */
   override suspend fun load(
      loadType: LoadType,
      state: PagingState<Int, QuoteResponseItem>
   ): MediatorResult {
      val page = INITIAL_PAGE_INDEX

      try {
         // Fetch quote data from the remote API
         val responseData = apiService.getQuote(page, state.config.pageSize)

         // Check if the end of pagination is reached
         val endOfPaginationReached = responseData.isEmpty()

         // Insert the fetched data into the local database
         database.withTransaction {
            if (loadType == LoadType.REFRESH) {
               // If it's a refresh, delete existing data
               database.quoteDao().deleteAll()
            }
            // Insert the retrieved data into the local database
            database.quoteDao().insertQuote(responseData)
         }

         // Return a success result along with information about the end of pagination
         return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
      } catch (exception: Exception) {
         // Return an error result if an exception occurs during data loading
         return MediatorResult.Error(exception)
      }
   }
}