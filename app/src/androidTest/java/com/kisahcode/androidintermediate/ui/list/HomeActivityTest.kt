package com.kisahcode.androidintermediate.ui.list

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.ui.detail.NewsDetailActivity
import com.kisahcode.androidintermediate.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeActivityTest{

    @get:Rule
    val activity = ActivityScenarioRule(HomeActivity::class.java)

    /**
     * Set up method to register EspressoIdlingResource for handling idling in Espresso tests.
     */
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * Tear down method to unregister EspressoIdlingResource after Espresso tests execution.
     */
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * Test case to verify the successful loading of headline news in the HomeActivity's RecyclerView.
     */
    @Test
    fun loadHeadlineNews_Success() {
        // Verifies that the RecyclerView displaying headline news is displayed.
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))

        // Scrolls to the 10th item in the RecyclerView.
        onView(withId(R.id.rv_news)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
    }

    /**
     * Test case to verify the successful loading of detailed news in the NewsDetailActivity.
     */
    @Test
    fun loadDetailNews_Success() {
        // Initialize Espresso Intents for intent verification.
        Intents.init()

        // Clicks on the first item in the RecyclerView to open the NewsDetailActivity.
        onView(withId(R.id.rv_news)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Verifies that the expected NewsDetailActivity is launched.
        intended(hasComponent(NewsDetailActivity::class.java.name))

        // Verifies that the WebView displaying news content is displayed.
        onView(withId(R.id.webView)).check(matches(isDisplayed()))

        // Finish Espresso Intents after intent verification.
        Intents.release()
    }

    /**
     * Test case to verify the successful loading of bookmarked news in the HomeActivity.
     */
    @Test
    fun loadBookmarkedNews_Success() {
        // Initialize Espresso Intents for intent verification.
        Intents.init()

        // Clicks on the first item in the RecyclerView to open the NewsDetailActivity.
        onView(withId(R.id.rv_news)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Clicks on the bookmark action button to bookmark the news.
        onView(withId(R.id.action_bookmark)).perform(click())

        // Presses the back button to return to the HomeActivity.
        pressBack()

        // Clicks on the BOOKMARK tab to switch to the bookmarked news list.
        onView(withText("BOOKMARK")).perform(click())

        // Checks if the RecyclerView displaying bookmarked news is displayed.
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))

        // Clicks on the first item in the RecyclerView to open the NewsDetailActivity.
        onView(withId(R.id.rv_news)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Verifies that the WebView displaying news content is displayed.
        onView(withId(R.id.webView)).check(matches(isDisplayed()))

        // Clicks on the bookmark action button to remove the bookmark.
        onView(withId(R.id.action_bookmark)).perform(click())

        // Presses the back button to return to the HomeActivity.
        pressBack()

        // Finish Espresso Intents after intent verification.
        Intents.release()
    }
}