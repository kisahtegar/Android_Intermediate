package com.kisahcode.androidintermediate.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Utility class for date formatting.
 *
 * This class provides a method to format a date string to a specified target time zone.
 * It uses the java.time package to parse the current date string into an Instant object
 * and then formats it using the specified target time zone.
 */
object DateFormatter {

    /**
     * Formats the current date string to the specified target time zone.
     *
     * This method takes a current date string and a target time zone as input parameters.
     * It parses the current date string into an Instant object, then formats it using
     * the specified target time zone with the pattern "dd MMM yyyy | HH:mm".
     *
     * @param currentDateString The current date string to be formatted.
     * @param targetTimeZone The target time zone to format the date string.
     * @return The formatted date string in the target time zone.
     */
    fun formatDate(currentDateString: String, targetTimeZone: String): String {
        // Parse the current date string into an Instant object
        val instant = Instant.parse(currentDateString)

        // Define a formatter with the pattern "dd MMM yyyy | HH:mm" and target time zone
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(targetTimeZone))

        // Format the instant using the formatter and return the formatted date string
        return formatter.format(instant)
    }
}