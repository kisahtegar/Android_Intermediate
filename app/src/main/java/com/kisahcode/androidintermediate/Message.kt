package com.kisahcode.androidintermediate

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Represents a message sent by a user.
 *
 * This data class encapsulates the properties of a message, including the text content,
 * sender's name, sender's photo URL, and the timestamp when the message was sent.
 * It is annotated with @IgnoreExtraProperties to indicate that additional properties
 * not defined in the class should be ignored during deserialization.
 *
 * @property text The text content of the message.
 * @property name The name of the sender.
 * @property photoUrl The URL of the sender's photo.
 * @property timestamp The timestamp when the message was sent.
 */
@IgnoreExtraProperties
data class Message(
   val text: String? = null,
   val name: String? = null,
   val photoUrl: String? = null,
   val timestamp: Long? = null
){
   // Null default values create a no-argument default constructor, which is needed
   // for deserialization from a DataSnapshot.
}