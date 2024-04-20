package com.kisahcode.androidintermediate.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kisahcode.androidintermediate.data.UserRepository
import com.kisahcode.androidintermediate.data.pref.UserModel
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling login-related operations.
 *
 * @param repository The UserRepository instance used for interacting with user session data.
 */
class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    /**
     * Saves the user session data.
     *
     * @param user The UserModel containing session data to be saved.
     */
    fun saveSession(user: UserModel) {
        // Launches a coroutine within the ViewModel's scope to asynchronously save the user session data
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}