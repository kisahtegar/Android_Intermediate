package com.kisahcode.androidintermediate.ui.detail

import androidx.lifecycle.*
import com.kisahcode.androidintermediate.data.NewsRepository
import com.kisahcode.androidintermediate.data.local.entity.NewsEntity
import kotlinx.coroutines.launch

class NewsDetailViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val newsData = MutableLiveData<NewsEntity>()

    fun setNewsData(news: NewsEntity) {
        newsData.value = news
    }

    val bookmarkStatus = newsData.switchMap {
        newsRepository.isNewsBookmarked(it.title)
    }

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