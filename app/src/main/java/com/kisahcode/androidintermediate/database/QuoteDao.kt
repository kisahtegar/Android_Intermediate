package com.kisahcode.androidintermediate.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kisahcode.androidintermediate.network.QuoteResponseItem

/**
 * Data Access Object (DAO) interface for managing quote-related database operations.
 */
@Dao
interface QuoteDao {

   /**
    * Inserts a list of quotes into the database.
    *
    * @param quote List of QuoteResponseItem objects to be inserted.
    */
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertQuote(quote: List<QuoteResponseItem>)

   /**
    * Retrieves all quotes from the database as a PagingSource.
    *
    * @return PagingSource object containing quotes.
    */
   @Query("SELECT * FROM quote")
   fun getAllQuote(): PagingSource<Int, QuoteResponseItem>

   /**
    * Deletes all quotes from the database.
    */
   @Query("DELETE FROM quote")
   suspend fun deleteAll()
}