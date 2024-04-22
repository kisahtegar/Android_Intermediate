package com.kisahcode.androidintermediate.data

/**
 * Represents the different states of a result from a data operation.
 *
 * It can be Success containing the result data, Error containing an error message,
 * or Loading indicating that the operation is in progress.
 *
 * @param R The type of data returned in the Success state.
 */
sealed class ResultState<out R> private constructor() {

    /**
     * Represents the Success state of the result.
     *
     * @property data The result data.
     * @constructor Creates a Success state with the specified data.
     */
    data class Success<out T>(val data: T) : ResultState<T>()

    /**
     * Represents the Error state of the result.
     *
     * @property error The error message.
     * @constructor Creates an Error state with the specified error message.
     */
    data class Error(val error: String) : ResultState<Nothing>()

    /**
     * Represents the Loading state of the result indicating that the operation is in progress.
     */
    data object Loading : ResultState<Nothing>()
}