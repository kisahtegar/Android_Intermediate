package com.kisahcode.androidintermediate

import java.util.*

/**
 * Utility object for generating random numbers.
 *
 * This object provides a method to generate a random integer within a specified range.
 */
internal object NumberGenerator {

    /**
     * Generates a random integer within the range [0, max).
     *
     * @param max The upper bound (exclusive) of the range for the generated number.
     * @return A random integer within the range [0, max).
     */
    fun generate(max: Int): Int {
        val random = Random()
        // Generate and return a random integer within the specified range
        return random.nextInt(max)
    }
}