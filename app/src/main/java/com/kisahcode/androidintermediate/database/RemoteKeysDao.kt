package com.kisahcode.androidintermediate.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object (DAO) interface for accessing and managing remote keys in the Room database.
 *
 * This interface defines methods for inserting, querying, and deleting remote keys.
 */
@Dao
interface RemoteKeysDao {

   /**
    * Inserts a list of remote keys into the database.
    *
    * @param remoteKey The list of remote keys to be inserted.
    */
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(remoteKey: List<RemoteKeys>)

   /**
    * Retrieves the remote key associated with the specified identifier.
    *
    * @param id The identifier of the remote key to retrieve.
    * @return The RemoteKeys object corresponding to the given identifier, if found; otherwise, null.
    */
   @Query("SELECT * FROM remote_keys WHERE id = :id")
   suspend fun getRemoteKeysId(id: String): RemoteKeys?

   /**
    * Deletes all remote keys from the database.
    */
   @Query("DELETE FROM remote_keys")
   suspend fun deleteRemoteKeys()
}