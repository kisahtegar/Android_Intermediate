package com.kisahcode.androidintermediate.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing remote keys used to keep track of pagination state.
 *
 * This class defines the schema for the remote keys table in the Room database.
 *
 * @param id The primary key identifier for the remote key.
 * @param prevKey The key corresponding to the previous page, if any.
 * @param nextKey The key corresponding to the next page, if any.
 */
@Entity(tableName = "remote_keys")
data class RemoteKeys(
   @PrimaryKey val id: String,
   val prevKey: Int?,
   val nextKey: Int?
)