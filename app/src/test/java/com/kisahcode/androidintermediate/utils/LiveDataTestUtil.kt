package com.kisahcode.androidintermediate.utils

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * A utility function to wait for LiveData to have an updated value.
 *
 * @param time The maximum time to wait.
 * @param timeUnit The time unit of the maximum time.
 * @param afterObserve An optional lambda to execute after observing LiveData.
 * @return The value of LiveData once it's been set.
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2, // Default timeout period set to 2 seconds
    timeUnit: TimeUnit = TimeUnit.SECONDS, // Default time unit set to seconds
    afterObserve: () -> Unit = {} // Optional lambda to execute after observing LiveData
): T {
    // Data variable to hold the value of LiveData
    var data: T? = null

    // CountDownLatch to wait until the value of LiveData is set
    val latch = CountDownLatch(1)

    val observer = object : Observer<T> {
        override fun onChanged(o: T) {
            // Assign the value of LiveData to the data variable
            data = o

            // Decrement the latch count to signal that the value has been set
            latch.countDown()

            // Remove the observer to prevent memory leaks
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    // Observe LiveData with the created observer
    this.observeForever(observer)

    try {
        // Execute the optional lambda after observing LiveData
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            // If the latch countdown reaches zero before the timeout period, throw a TimeoutException
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        // Remove the observer after the value of LiveData is set
        this.removeObserver(observer)
    }

    // Return the value of LiveData once it's been set
    @Suppress("UNCHECKED_CAST")
    return data as T
}

/**
 * A suspend function to observe LiveData for testing purposes.
 *
 * @param block The block of code to execute after observing LiveData.
 */
suspend fun <T> LiveData<T>.observeForTesting(block: suspend  () -> Unit) {
    // Create a dummy observer
    val observer = Observer<T> { }

    try {
        // Observe LiveData with the dummy observer
        observeForever(observer)

        // Execute the suspend lambda block
        block()
    } finally {
        // Remove the observer after executing the block
        removeObserver(observer)
    }
}