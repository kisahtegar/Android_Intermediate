package com.kisahcode.androidintermediate.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData

import com.kisahcode.androidintermediate.utils.DataDummy
import com.kisahcode.androidintermediate.data.NewsRepository
import com.kisahcode.androidintermediate.data.Result
import com.kisahcode.androidintermediate.data.local.entity.NewsEntity
import com.kisahcode.androidintermediate.utils.getOrAwaitValue

import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for the [NewsViewModel] class.
 */
@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {

    // Executes tasks synchronously using Architecture Components.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Mocked NewsRepository to simulate its behavior during testing.
    @Mock
    private lateinit var newsRepository: NewsRepository

    // The ViewModel under test.
    private lateinit var newsViewModel: NewsViewModel

    // Dummy list of news entities for testing.
    private val dummyNews = DataDummy.generateDummyNewsEntity()

    /**
     * Set up the ViewModel and its dependencies before each test execution.
     */
    @Before
    fun setUp() {
        newsViewModel = NewsViewModel(newsRepository)
    }

    /**
     * Test case to verify that when fetching headline news, it should not return null and
     * return a success result containing the expected list of news entities.
     */
    @Test
    fun `when Get HeadlineNews Should Not Null and Return Success`() {
        // Create a LiveData object with the expected result.
        val expectedNews = MutableLiveData<Result<List<NewsEntity>>>()
        expectedNews.value = Result.Success(dummyNews)

        // Mock the behavior of the news repository to return the expected result.
        `when`(newsRepository.getHeadlineNews()).thenReturn(expectedNews)

        // Call the method under test and get the actual result.
        val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()

        // Verify that the repository method was called.
        Mockito.verify(newsRepository).getHeadlineNews()

        // Assertions to verify the correctness of the result.
        Assert.assertNotNull(actualNews)
        Assert.assertTrue(actualNews is Result.Success)
        Assert.assertEquals(dummyNews.size, (actualNews as Result.Success).data.size)
    }

    /**
     * Test case to verify that when a network error occurs, the ViewModel should return an
     * error result.
     */
    @Test
    fun `when Network Error Should Return Error`() {
        // Create a LiveData object with an error result.
        val headlineNews = MutableLiveData<Result<List<NewsEntity>>>()
        headlineNews.value = Result.Error("Error")

        // Mock the behavior of the news repository to return the error result.
        `when`(newsRepository.getHeadlineNews()).thenReturn(headlineNews)

        // Call the method under test and get the actual result.
        val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()

        // Verify that the repository method was called.
        Mockito.verify(newsRepository).getHeadlineNews()

        // Assertions to verify that the actual result is an error.
        Assert.assertNotNull(actualNews)
        Assert.assertTrue(actualNews is Result.Error)
    }
}