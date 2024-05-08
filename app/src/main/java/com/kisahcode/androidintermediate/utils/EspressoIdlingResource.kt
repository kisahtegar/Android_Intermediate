package com.kisahcode.androidintermediate.utils

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Utility class for managing Espresso idling resources to synchronize asynchronous operations with Espresso.
 */
object EspressoIdlingResource {

    // Unique identifier for the idling resource.
    private const val RESOURCE = "GLOBAL"

    // Instance of CountingIdlingResource for tracking asynchronous operations.
    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    /**
     * Increments the counter of active operations, indicating that the app is busy.
     */
    fun increment() {
        countingIdlingResource.increment()
    }

    /**
     * Decrements the counter of active operations, indicating that the app is idle if the counter is zero.
     */
    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

/**
 * Executes the provided function while managing the Espresso idling resource.
 *
 * @param function The function to be executed while managing the idling resource.
 * @return The result of the provided function.
 */
inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    EspressoIdlingResource.increment() // Set app as busy.
    return try {
        function() // Execute the provided function.
    } finally {
        EspressoIdlingResource.decrement() // Set app as idle.
    }
}