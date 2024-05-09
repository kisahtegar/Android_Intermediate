package com.kisahcode.androidintermediate.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * StudentDatabase serves as the main access point for the underlying Room database.
 * It provides methods to retrieve DAOs and access the data stored in the database.
 *
 * @property studentDao DAO interface for accessing student-related data.
 */
@Database(entities = [Student::class, University::class, Course::class, CourseStudentCrossRef::class], version = 1, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    companion object {
        // Volatile ensures that INSTANCE variable is always up-to-date and visible to all threads.
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        /**
         * Retrieves the singleton instance of the StudentDatabase.
         *
         * If the instance does not exist, it creates a new one.
         *
         * @param context Application context.
         * @return Singleton instance of the StudentDatabase.
         */
        @JvmStatic
        fun getDatabase(context: Context): StudentDatabase {
            if (INSTANCE == null) {
                synchronized(StudentDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            StudentDatabase::class.java, "student_database")
                        .fallbackToDestructiveMigration(false)
                        .build()
                }
            }
            return INSTANCE as StudentDatabase
        }

    }
}