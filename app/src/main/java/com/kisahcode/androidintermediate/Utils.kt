/**
 * Utility class for handling image capture and retrieval URIs.
 * Provides methods to obtain URIs for captured images.
 */

package com.kisahcode.androidintermediate

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// File name format for captured images.
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

// Timestamp for naming captured image files.
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

/**
 * Retrieves the URI for the captured image.
 *
 * If the device runs on Android Q (API level 29) or higher, it inserts the image into the MediaStore.
 * Otherwise, it saves the image in the application-specific external storage directory.
 *
 * @param context The context of the application.
 * @return The URI of the captured image.
 */
fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // For Android Q (API level 29) and higher, insert the image into MediaStore.
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        // content://media/external/images/media/1000000062
        // storage/emulated/0/Pictures/MyCamera/20230825_155303.jpg
    }

    // If the URI is null (for pre-Android Q), get the image URI from the application-specific external storage.
    return uri ?: getImageUriForPreQ(context)
}

/**
 * Retrieves the URI for the captured image for devices running on Android versions prior to Q (API level 29).
 *
 * @param context The context of the application.
 * @return The URI of the captured image.
 */
private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
    // Create the directory if it doesn't exist.
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    // Get the URI for the image using FileProvider for compatibility.
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
    //content://com.dicoding.picodiploma.mycamera.fileprovider/my_images/MyCamera/20230825_133659.jpg
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}