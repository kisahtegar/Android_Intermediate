package com.kisahcode.androidintermediate

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import okio.IOException
import java.io.InputStreamReader

/**
 * Utility object for reading JSON files from the assets directory.
 */
object JsonConverter {

    /**
     * Reads the contents of a JSON file and returns it as a string.
     *
     * @param fileName The name of the JSON file to read.
     * @return The contents of the JSON file as a string.
     * @throws IOException If an I/O error occurs while reading the file.
     */
   fun readStringFromFile(fileName: String): String {
       try {
           // Get the application context.
           val applicationContext = ApplicationProvider.getApplicationContext<Context>()

           // Open the JSON file as an input stream.
           val inputStream = applicationContext.assets.open(fileName)

           // Create a StringBuilder to store the contents of the file.
           val builder = StringBuilder()

           // Create a InputStreamReader to read the contents of the file.
           val reader = InputStreamReader(inputStream, "UTF-8")

           // Read each line of the file and append it to the StringBuilder.
           reader.readLines().forEach {
               builder.append(it)
           }

           // Return the contents of the file as a string.
           return builder.toString()
       } catch (e: IOException) {
           // If an IOException occurs, rethrow it.
           throw e
       }
   }
}