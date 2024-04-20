package com.kisahcode.androidintermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kisahcode.androidintermediate.data.UserRepository
import com.kisahcode.androidintermediate.data.pref.UserModel
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling main activity-related operations.
 *
 * @param repository The UserRepository instance used for interacting with user session data.
 */
class MainViewModel(private val repository: UserRepository) : ViewModel() {

    /**
     * Retrieves the user session data as LiveData.
     *
     * @return LiveData containing the user session data.
     */
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    /**
     * Logs out the user.
     *
     * This function launches a coroutine to perform the logout operation asynchronously.
     */
    fun logout() {
        // Launch a coroutine within the ViewModel's scope to perform the logout operation
        viewModelScope.launch {
            repository.logout()
        }
    }

}