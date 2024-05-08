package com.kisahcode.androidintermediate.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kisahcode.androidintermediate.data.local.room.NewsDao
import com.kisahcode.androidintermediate.data.remote.retrofit.ApiService
import com.kisahcode.androidintermediate.utils.DataDummy
import com.kisahcode.androidintermediate.utils.MainDispatcherRule
import com.kisahcode.androidintermediate.utils.getOrAwaitValue
import com.kisahcode.androidintermediate.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the NewsRepository class.
 */
@ExperimentalCoroutinesApi
class NewsRepositoryTest {

    // Executes tasks in the Architecture Components in the same thread.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Provides a test coroutine dispatcher.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mock ApiService, NewsDao, and NewsRepository instances.
    private lateinit var apiService: ApiService
    private lateinit var newsDao: NewsDao
    private lateinit var newsRepository: NewsRepository

    /**
     * Initializes ApiService, NewsDao, and NewsRepository instances before each test.
     */
    @Before
    fun setUp() {
        apiService = FakeApiService()
        newsDao = FakeNewsDao()
        newsRepository = NewsRepository(apiService, newsDao)
    }

    /**
     * Test case to verify that the [NewsRepository.getHeadlineNews] method returns a non-null
     * LiveData containing news data.
     */
    @Test
    fun `when getHeadlineNews Should Not Null`() = runTest {
        // Generate dummy news response for testing.
        val expectedNews = DataDummy.generateDummyNewsResponse()

        // Call the getHeadlineNews method to retrieve the news data.
        val actualNews = newsRepository.getHeadlineNews()

        // Observe the LiveData to ensure it is not null and contains the expected number of articles.
        actualNews.observeForTesting {
            // Assert that the LiveData is not null.
            Assert.assertNotNull(actualNews)

            // Assert that the number of articles in the LiveData matches the number of articles
            // in the expected news response.
            Assert.assertEquals(
                expectedNews.articles.size,
                (actualNews.value as Result.Success).data.size
            )
        }
    }

    /**
     * Test case to verify that when a news item is saved using [NewsRepository.saveNews],
     * it exists in the list of bookmarked news retrieved using [NewsRepository.getBookmarkedNews].
     */
    @Test
    fun `when saveNews Should Exist in getBookmarkedNews`() = runTest {
        // Generate a sample news entity for testing.
        val sampleNews = DataDummy.generateDummyNewsEntity()[0]

        // Save the sample news item using the repository.
        newsDao.saveNews(sampleNews)

        // Retrieve the list of bookmarked news items using the repository.
        val actualNews = newsRepository.getBookmarkedNews().getOrAwaitValue()

        // Assert that the list of bookmarked news contains the sample news item.
        Assert.assertTrue(actualNews.contains(sampleNews))
        // Assert that the news item is bookmarked (exists) in the repository.
        Assert.assertTrue(newsRepository.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }

    /**
     * Test case to verify that when a news item is deleted using [NewsRepository.deleteNews],
     * it does not exist in the list of bookmarked news retrieved using [NewsRepository.getBookmarkedNews].
     */
    @Test
    fun `when deleteNews Should Not Exist in getBookmarkedNews`() = runTest {
        // Generate a sample news entity for testing.
        val sampleNews = DataDummy.generateDummyNewsEntity()[0]

        // Save the sample news item using the repository.
        newsRepository.saveNews(sampleNews)

        // Delete the sample news item using the repository.
        newsRepository.deleteNews(sampleNews.title)

        // Retrieve the list of bookmarked news items using the repository.
        val actualNews = newsRepository.getBookmarkedNews().getOrAwaitValue()

        // Assert that the list of bookmarked news does not contain the sample news item.
        Assert.assertFalse(actualNews.contains(sampleNews))

        // Assert that the news item is not bookmarked (does not exist) in the repository.
        Assert.assertFalse(newsRepository.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }
}