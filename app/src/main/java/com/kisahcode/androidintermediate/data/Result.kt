package com.kisahcode.androidintermediate.data

/**
 * A sealed class representing the result of an operation.
 *
 * This class provides three possible states: Success, Error, and Loading.
 * - Success holds the result data of a successful operation.
 * - Error holds an error message in case of a failed operation.
 * - Loading indicates that the operation is still in progress.
 *
 * @param R The type of data contained in the Success result.
 */
sealed class Result<out R> private constructor() {

    /**
     * Represents a successful operation with the result data.
     *
     * @param data The data resulting from a successful operation.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents a failed operation with an error message.
     *
     * @param error The error message describing the failure.
     */
    data class Error(val error: String) : Result<Nothing>()

    /**
     * Represents a loading state indicating that the operation is in progress.
     */
    data object Loading : Result<Nothing>()
}