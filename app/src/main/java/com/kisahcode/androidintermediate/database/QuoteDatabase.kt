package com.kisahcode.androidintermediate.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kisahcode.androidintermediate.network.QuoteResponseItem

/**
 * Room database class for storing quotes obtained from the network.
 *
 * This class defines the database configuration and serves as the main access point for interacting
 * with the quotes data.
 */
@Database(
    entities = [QuoteResponseItem::class],
    version = 1,
    exportSchema = false
)
abstract class QuoteDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: QuoteDatabase? = null

        /**
         * Retrieves an instance of the QuoteDatabase.
         *
         * @param context The application context.
         * @return An instance of the QuoteDatabase.
         */
        @JvmStatic
        fun getDatabase(context: Context): QuoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java, "quote_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}