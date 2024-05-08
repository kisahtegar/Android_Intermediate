package com.kisahcode.androidintermediate.ui.detail

import androidx.lifecycle.*
import com.kisahcode.androidintermediate.data.NewsRepository
import com.kisahcode.androidintermediate.data.local.entity.NewsEntity
import kotlinx.coroutines.launch

/**
 * ViewModel class for managing news detail data and bookmark status.
 *
 * This ViewModel class provides methods to set news data, retrieve bookmark status, and change the
 * bookmark status of a news article.
 *
 * @param newsRepository The repository providing access to news data.
 */
class NewsDetailViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val newsData = MutableLiveData<NewsEntity>()

    /**
     * Sets the news data for the detail view.
     *
     * This method sets the news data to be observed by the detail view.
     *
     * @param news The news entity containing the detail data.
     */
    fun setNewsData(news: NewsEntity) {
        newsData.value = news
    }

    /**
     * LiveData representing the bookmark status of the news article.
     *
     * This LiveData property observes changes in the bookmark status of the news article.
     * It switches based on the news data and retrieves the bookmark status from the repository.
     */
    val bookmarkStatus = newsData.switchMap {
        newsRepository.isNewsBookmarked(it.title)
    }

    /**
     * Changes the bookmark status of the news article.
     *
     * This method changes the bookmark status of the news article based on its current status.
     * If the article is bookmarked, it will be removed from the database. If not, it will be saved.
     *
     * @param newsDetail The news entity representing the detail data.
     */
    fun changeBookmark(newsDetail: NewsEntity) {
        viewModelScope.launch {
            if (bookmarkStatus.value as Boolean) {
                newsRepository.deleteNews(newsDetail.title)
            } else {
                newsRepository.saveNews(newsDetail)
            }
        }
    }
}