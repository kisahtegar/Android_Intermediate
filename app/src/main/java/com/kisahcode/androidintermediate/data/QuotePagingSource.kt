package com.kisahcode.androidintermediate.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kisahcode.androidintermediate.network.ApiService
import com.kisahcode.androidintermediate.network.QuoteResponseItem

/**
 * Paging source responsible for loading pages of quote data from the API.
 *
 * This class implements the PagingSource interface to load paginated data from the API. It fetches
 * quotes from the API in response to load requests, handling pagination and error cases.
 *
 * @property apiService The ApiService instance used for making network requests.
 */
class QuotePagingSource(private val apiService: ApiService) : PagingSource<Int, QuoteResponseItem>() {

    private companion object {
        /** The initial page index for pagination. */
        const val INITIAL_PAGE_INDEX = 1
    }

    /**
     * Loads a page of quote data from the API.
     *
     * This function is called by the Paging library when it needs to load a page of data. It
     * retrieves quotes from the API based on the provided page number and page size, handling
     * pagination and error cases.
     *
     * @param params The parameters for loading the page, including the page number and page size.
     * @return A LoadResult object containing the loaded data, previous and next page keys, or an error.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, QuoteResponseItem> {
        return try {
            // Determine the page number to load, defaulting to the initial page index if not specified
            val page = params.key ?: INITIAL_PAGE_INDEX

            // Make a network request to fetch quotes from the API for the specified page
            val responseData = apiService.getQuote(page, params.loadSize)

            // Construct a LoadResult with the loaded data, previous and next page keys
            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            // If an error occurs during loading, return an error result
            return LoadResult.Error(exception)
        }
    }

    /**
     * Returns the refresh key for the current PagingState.
     *
     * This function calculates the refresh key based on the current anchor position in the PagingState.
     * The refresh key is used to determine the position of the anchor item when refreshing the data set,
     * ensuring a smooth user experience when new data is loaded.
     *
     * @param state The current PagingState, containing information about the loaded data and the anchor position.
     * @return The refresh key, indicating the position of the anchor item.
     */
    override fun getRefreshKey(state: PagingState<Int, QuoteResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // Find the closest page to the anchor position
            val anchorPage = state.closestPageToPosition(anchorPosition)

            // Calculate the refresh key based on the previous or next page key of the anchor page
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}