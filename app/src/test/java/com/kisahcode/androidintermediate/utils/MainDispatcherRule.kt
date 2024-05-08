package com.kisahcode.androidintermediate.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit rule to override the main coroutine dispatcher with a test dispatcher.
 *
 * This rule is useful for testing coroutines that use [Dispatchers.Main].
 *
 * @param testDispatcher The test dispatcher to be used for overriding the main dispatcher.
 */
@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    /**
     * Sets up the test environment before each test execution.
     *
     * Overrides the main coroutine dispatcher with the provided [testDispatcher].
     * This ensures that any coroutine launched in the test runs on the test dispatcher,
     * which is suitable for testing asynchronous code that relies on the main dispatcher.
     *
     * @param description The description of the test being executed.
     *                    This parameter is provided by JUnit and contains information
     *                    about the test method and class.
     */
    override fun starting(description: Description) {
        // Set the main coroutine dispatcher to the test dispatcher.
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Cleans up the test environment after each test execution.
     *
     * Resets the main coroutine dispatcher to its original state, ensuring that subsequent tests
     * do not inherit the test dispatcher.
     *
     * @param description The description of the test that has finished execution.
     *                    This parameter is provided by JUnit and contains information
     *                    about the test method and class.
     */
    override fun finished(description: Description) {
        // Reset the main coroutine dispatcher to its original state.
        Dispatchers.resetMain()
    }
}
