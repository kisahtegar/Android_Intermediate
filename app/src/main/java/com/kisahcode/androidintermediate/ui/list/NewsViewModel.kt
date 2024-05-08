package com.kisahcode.androidintermediate.ui.list

import androidx.lifecycle.ViewModel
import com.kisahcode.androidintermediate.data.NewsRepository

/**
 * ViewModel responsible for providing data to the NewsListFragment.
 *
 * This ViewModel communicates with the repository to fetch both headline news and bookmarked news.
 * It exposes methods to retrieve headline news and bookmarked news.
 *
 * @param newsRepository The repository responsible for fetching news data.
 */
class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    /**
     * Retrieves the headline news from the repository.
     *
     * @return Returns LiveData containing the result of fetching headline news.
     */
    fun getHeadlineNews() = newsRepository.getHeadlineNews()

    /**
     * Retrieves the bookmarked news from the repository.
     *
     * @return Returns LiveData containing the bookmarked news.
     */
    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()
}