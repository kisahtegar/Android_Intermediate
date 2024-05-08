package com.kisahcode.androidintermediate.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kisahcode.androidintermediate.data.local.entity.NewsEntity

/**
 * Room database class for managing news data.
 *
 * This class defines the database configuration, including the entities it contains and the version number.
 * It also provides a method to obtain an instance of the database using the singleton pattern.
 */
@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    /**
     * Returns a DAO (Data Access Object) interface for accessing news data in the database.
     *
     * @return The DAO interface for news data access.
     */
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var instance: NewsDatabase? = null

        /**
         * Gets an instance of the NewsDatabase.
         *
         * This method creates a singleton instance of the NewsDatabase if it doesn't exist,
         * or returns the existing instance if it does.
         *
         * @param context The application context.
         * @return The NewsDatabase instance.
         */
        fun getInstance(context: Context): NewsDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java, "News.db"
                ).build()
            }
    }
}