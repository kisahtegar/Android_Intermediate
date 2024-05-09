package com.kisahcode.androidintermediate

import android.app.Application
import com.kisahcode.androidintermediate.database.StudentDatabase

/**
 * Custom Application class responsible for initializing global components and services.
 * This class is instantiated when the application is starting up.
 */
class MyApplication : Application() {
    // Lazily initialize the database instance
    val database by lazy { StudentDatabase.getDatabase(this) }

    // Lazily initialize the repository instance using the database's DAO
    val repository by lazy { StudentRepository(database.studentDao())}
}