package com.kisahcode.androidintermediate.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData

import com.kisahcode.androidintermediate.data.NewsRepository
import com.kisahcode.androidintermediate.utils.DataDummy
import com.kisahcode.androidintermediate.utils.MainDispatcherRule
import com.kisahcode.androidintermediate.utils.getOrAwaitValue

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for [NewsDetailViewModel].
 *
 * These tests validate the behavior of [NewsDetailViewModel] by mocking the [NewsRepository].
 * The tests cover scenarios related to changing the bookmark status of a news item.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NewsDetailViewModelTest{

    /**
     * Rule to ensure LiveData tasks are executed synchronously on the main thread.
     * This facilitates testing LiveData interactions.
     */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    /**
     * Mocked [NewsRepository] to simulate data source interactions.
     */
    @Mock
    private lateinit var newsRepository: NewsRepository

    /**
     * Instance of [NewsDetailViewModel] under test.
     */
    private lateinit var newsDetailViewModel: NewsDetailViewModel

    /**
     * Dummy news entity used for testing.
     */
    private val dummyDetailNews = DataDummy.generateDummyNewsEntity()[0]

    /**
     * Sets up the test environment before each test case.
     *
     * Initializes [newsDetailViewModel] with [newsRepository] and sets the dummy news data.
     */
    @Before
    fun setUp() {
        newsDetailViewModel = NewsDetailViewModel(newsRepository)
        newsDetailViewModel.setNewsData(dummyDetailNews)
    }

    /**
     * Rule to set the main coroutine dispatcher to a test dispatcher.
     *
     * This allows testing suspending functions asynchronously in a controlled environment.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    /**
     * Test case to verify that when the bookmark status is false,
     *
     * calling [changeBookmark] should invoke [saveNews] on [newsRepository].
     */
    @Test
    fun `when bookmarkStatus false Should call saveNews`() = runTest {
        // Mock the repository response to indicate that the news is not bookmarked
        val expectedBoolean = MutableLiveData<Boolean>().apply { value = false }
        `when`(newsRepository.isNewsBookmarked(dummyDetailNews.title)).thenReturn(expectedBoolean)

        // Trigger the changeBookmark method
        newsDetailViewModel.bookmarkStatus.getOrAwaitValue()
        newsDetailViewModel.changeBookmark(dummyDetailNews)

        // Verify that saveNews is called on the repository
        Mockito.verify(newsRepository).saveNews(dummyDetailNews)
    }

    /**
     * Test case to verify that when the bookmark status is true,
     *
     * calling [changeBookmark] should invoke [deleteNews] on [newsRepository].
     */
    @Test
    fun `when bookmarkStatus true Should call deleteNews`() = runTest {
        // Mock the repository response to indicate that the news is bookmarked
        val expectedBoolean = MutableLiveData<Boolean>().apply { value = true }
        `when`(newsRepository.isNewsBookmarked(dummyDetailNews.title)).thenReturn(expectedBoolean)

        // Trigger the changeBookmark method
        newsDetailViewModel.bookmarkStatus.getOrAwaitValue()
        newsDetailViewModel.changeBookmark(dummyDetailNews)

        // Verify that deleteNews is called on the repository
        Mockito.verify(newsRepository).deleteNews(dummyDetailNews.title)
    }
}