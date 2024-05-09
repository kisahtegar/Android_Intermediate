package com.kisahcode.androidintermediate

import android.app.Application
import com.kisahcode.androidintermediate.database.StudentDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Custom Application class responsible for initializing global components and services.
 * This class is instantiated when the application is starting up.
 */
class MyApplication : Application() {

    // Coroutine scope for managing asynchronous operations
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Lazily initialize the database instance
    val database by lazy { StudentDatabase.getDatabase(this, applicationScope) }

    // Lazily initialize the repository instance using the database's DAO
    val repository by lazy { StudentRepository(database.studentDao())}
}