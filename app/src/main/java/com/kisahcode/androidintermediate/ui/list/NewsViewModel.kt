package com.kisahcode.androidintermediate.ui.list

import androidx.lifecycle.ViewModel
import com.kisahcode.androidintermediate.data.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getHeadlineNews() = newsRepository.getHeadlineNews()

    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()
}