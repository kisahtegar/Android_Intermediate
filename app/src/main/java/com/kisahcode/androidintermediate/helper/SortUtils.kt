package com.kisahcode.androidintermediate.helper

import androidx.sqlite.db.SimpleSQLiteQuery

/**
 * A utility class for generating sorted SQL queries for database operations.
 */
object SortUtils {

    /**
     * Generates a sorted SQL query based on the specified sorting type.
     *
     * @param sortType The type of sorting to be applied.
     * @return A SimpleSQLiteQuery object representing the sorted SQL query.
     */
    fun getSortedQuery(sortType: SortType): SimpleSQLiteQuery {
        // Initialize a StringBuilder to construct the SQL query.
        val simpleQuery = StringBuilder().append("SELECT * FROM student ")

        // Append the appropriate ORDER BY clause based on the sorting type.
        when (sortType) {
            SortType.ASCENDING -> {
                simpleQuery.append("ORDER BY name ASC")
            }
            SortType.DESCENDING -> {
                simpleQuery.append("ORDER BY name DESC")
            }
            SortType.RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
        }

        // Create a SimpleSQLiteQuery object with the generated SQL query and return it.
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}