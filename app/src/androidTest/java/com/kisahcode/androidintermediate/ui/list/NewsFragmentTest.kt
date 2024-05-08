package com.kisahcode.androidintermediate.ui.list

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.kisahcode.androidintermediate.JsonConverter
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.data.remote.retrofit.ApiConfig
import com.kisahcode.androidintermediate.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration tests for the NewsFragment class using MockWebServer and Idling Resource.
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
class NewsFragmentTest {

    private val mockWebServer = MockWebServer()

    /**
     * Sets up the environment before each test case execution.
     */
    @Before
    fun setUp() {
        // Start the MockWebServer.
        mockWebServer.start(8080)
        // Configure the base URL for the API to the local MockWebServer address.
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        // Register the EspressoIdlingResource to notify Espresso about background tasks.
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * Cleans up the environment after each test case execution.
     */
    @After
    fun tearDown() {
        // Shut down the MockWebServer to release resources.
        mockWebServer.shutdown()
        // Unregister the EspressoIdlingResource to indicate the end of background tasks.
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * Test case to verify the successful retrieval of headline news.
     */
    @Test
    fun getHeadlineNews_Success() {
        // Prepare the arguments for launching the fragment.
        val bundle = Bundle()
        bundle.putString(NewsFragment.ARG_TAB, NewsFragment.TAB_NEWS)

        // Launches the NewsFragment in a container with the specified theme and arguments.
        launchFragmentInContainer<NewsFragment>(bundle, R.style.Theme_AndroidIntermediate)

        // Enqueue a mock response with a success status code and a JSON body containing sample news data.
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        // Checks if the RecyclerView displaying news items is displayed.
        onView(withId(R.id.rv_news))
            .check(matches(isDisplayed()))

        // Checks if a specific news item with a title is displayed on the screen.
        onView(withText("Inti Bumi Mendingin Lebih Cepat, Pertanda Apa? - detikInet"))
            .check(matches(isDisplayed()))

        // Scrolls to a specific news item with a title and checks if it is displayed on the screen.
        onView(withId(R.id.rv_news))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Perjalanan Luar Angkasa Sebabkan Anemia - CNN Indonesia"))
                )
            )
    }

    /**
     * Test case to verify the handling of an error response when retrieving headline news.
     */
    @Test
    fun getHeadlineNews_Error() {
        // Prepare the arguments for launching the fragment.
        val bundle = Bundle()
        bundle.putString(NewsFragment.ARG_TAB, NewsFragment.TAB_NEWS)

        // Launches the NewsFragment in a container with the specified theme and arguments.
        launchFragmentInContainer<NewsFragment>(bundle, R.style.Theme_AndroidIntermediate)

        // Enqueue a mock response with a server error status code.
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        // Checks if the error message TextView is displayed on the screen.
        onView(withId(R.id.tv_error))
            .check(matches(isDisplayed()))

        // Verifies that the error message "Oops.. something went wrong." is displayed.
        onView(withText("Oops.. something went wrong."))
            .check(matches(isDisplayed()))
    }
}