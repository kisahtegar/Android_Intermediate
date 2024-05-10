package com.kisahcode.androidintermediate.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kisahcode.androidintermediate.database.QuoteDatabase
import com.kisahcode.androidintermediate.database.RemoteKeys
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
      // Determine the page to be loaded based on the load type
      val page = when (loadType) {
         LoadType.REFRESH ->{
            // For a refresh, get the next key closest to the current position
            val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
            // Calculate the page based on the next key
            remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
         }
         LoadType.PREPEND -> {
            // For prepending data, get the previous key for the first item
            val remoteKeys = getRemoteKeyForFirstItem(state)
            // Calculate the previous key
            val prevKey = remoteKeys?.prevKey
               ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            prevKey
         }
         LoadType.APPEND -> {
            // For appending data, get the next key for the last item
            val remoteKeys = getRemoteKeyForLastItem(state)
            // Calculate the next key
            val nextKey = remoteKeys?.nextKey
               ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            nextKey
         }
      }

      try {
         // Fetch quote data from the remote API
         val responseData = apiService.getQuote(page, state.config.pageSize)

         // Check if the end of pagination is reached
         val endOfPaginationReached = responseData.isEmpty()

         // Insert the fetched data into the local database
         database.withTransaction {
            if (loadType == LoadType.REFRESH) {
               // If it's a refresh, delete existing data and remote keys
               database.remoteKeysDao().deleteRemoteKeys()
               database.quoteDao().deleteAll()
            }

            // Calculate previous and next keys for the fetched data
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1

            // Map the fetched data to RemoteKeys and insert them into the database
            val keys = responseData.map {
               RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
            }
            database.remoteKeysDao().insertAll(keys)

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

   /**
    * Retrieves the remote key for the last item loaded in the current page.
    *
    * @param state The current PagingState, including information about the currently loaded data.
    * @return The RemoteKeys object associated with the last item, or null if not found.
    */
   private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, QuoteResponseItem>): RemoteKeys? {
      return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
         database.remoteKeysDao().getRemoteKeysId(data.id)
      }
   }

   /**
    * Retrieves the remote key for the first item loaded in the current page.
    *
    * @param state The current PagingState, including information about the currently loaded data.
    * @return The RemoteKeys object associated with the first item, or null if not found.
    */
   private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, QuoteResponseItem>): RemoteKeys? {
      return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
         database.remoteKeysDao().getRemoteKeysId(data.id)
      }
   }

   /**
    * Retrieves the remote key closest to the current position in the list.
    *
    * @param state The current PagingState, including information about the currently loaded data.
    * @return The RemoteKeys object associated with the item closest to the current position, or null if not found.
    */
   private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, QuoteResponseItem>): RemoteKeys? {
      return state.anchorPosition?.let { position ->
         state.closestItemToPosition(position)?.id?.let { id ->
            database.remoteKeysDao().getRemoteKeysId(id)
         }
      }
   }
}