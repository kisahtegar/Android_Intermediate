package com.kisahcode.androidintermediate.data.local.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kisahcode.androidintermediate.utils.DataDummy
import com.kisahcode.androidintermediate.utils.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Integration test class to verify the functionality of [NewsDao] using an in-memory Room database.
 */
class NewsDaoTest{

    // A JUnit Test Rule that swaps the background executor used by the Architecture Components
    // with a different one which executes each task synchronously.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Reference to the in-memory Room database instance.
    private lateinit var database: NewsDatabase

    // Reference to the DAO (Data Access Object) under test.
    private lateinit var dao: NewsDao

    // Sample news entity used for testing.
    private val sampleNews = DataDummy.generateDummyNewsEntity()[0]

    /**
     * Setup method to initialize the in-memory Room database and obtain an instance of [NewsDao].
     */
    @Before
    fun initDb() {
        // Create an in-memory Room database instance using a builder pattern.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), // Provides a context for the database creation.
            NewsDatabase::class.java // Specifies the class of the Room database.
        ).build()

        // Obtain an instance of the NewsDao interface from the Room database.
        dao = database.newsDao()
    }

    /**
     * Teardown method to close the in-memory Room database after each test execution.
     */
    @After
    fun closeDb() = database.close()

    /**
     * Test case to verify that saving a news item using [NewsDao.saveNews] is successful.
     */
    @Test
    fun saveNews_Success() = runTest {
        // Save the sample news item using the saveNews method of the DAO.
        dao.saveNews(sampleNews)

        // Retrieve the list of bookmarked news items from the DAO and await its value.
        val actualNews = dao.getBookmarkedNews().getOrAwaitValue()

        // Assert that the title of the saved news item matches the title of the retrieved news item.
        Assert.assertEquals(sampleNews.title, actualNews[0].title)

        // Assert that the saved news item is bookmarked by checking its existence in the database.
        Assert.assertTrue(dao.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }

    /**
     * Test case to verify that deleting a news item using [NewsDao.deleteNews] is successful.
     */
    @Test
    fun deleteNews_Success() = runTest {
        // Save the sample news item using the saveNews method of the DAO.
        dao.saveNews(sampleNews)

        // Delete the sample news item using the deleteNews method of the DAO.
        dao.deleteNews(sampleNews.title)

        // Retrieve the list of bookmarked news items from the DAO and await its value.
        val actualNews = dao.getBookmarkedNews().getOrAwaitValue()

        // Assert that the list of bookmarked news items is empty after deletion.
        Assert.assertTrue(actualNews.isEmpty())

        // Assert that the deleted news item is no longer bookmarked in the database.
        Assert.assertFalse(dao.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }
}