/**
 * Utility class for handling image capture and retrieval URIs.
 * Provides methods to obtain URIs for captured images.
 */

package com.kisahcode.androidintermediate

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// File name format for captured images.
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

// Timestamp for naming captured image files.
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

// Maximum size threshold for the compressed image file.
private const val MAXIMAL_SIZE = 1000000

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

/**
 * Creates a custom temporary file with a unique name in the external cache directory of the app.
 *
 * @param context The context used to access the external cache directory.
 * @return A File object representing the created temporary file.
 */
fun createCustomTempFile(context: Context): File {
    // Get the external cache directory of the app.
    val filesDir = context.externalCacheDir

    // Create a temporary file with a unique name and ".jpg" extension in the cache directory.
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

/**
 * Converts a given [imageUri] to a File object.
 *
 * @param imageUri The URI of the image to convert.
 * @param context The context used to access content resolver and file operations.
 * @return A File object representing the image corresponding to the provided URI.
 */
fun uriToFile(imageUri: Uri, context: Context): File {
    // Create a temporary file to store the converted image.
    val myFile = createCustomTempFile(context)

    // Open an input stream to read data from the image URI.
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream

    // Open an output stream to write data to the temporary file.
    val outputStream = FileOutputStream(myFile)

    // Create a buffer to read data from the input stream.
    val buffer = ByteArray(1024)
    var length: Int

    // Read data from the input stream and write it to the output stream until the end of the stream is reached.
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)

    // Close both input and output streams.
    outputStream.close()
    inputStream.close()

    // Return the created File object.
    return myFile
}

/**
 * Reduces the size of the image file to meet a certain maximum size threshold.
 *
 * This function compresses the image file until its size is within the specified maximal size threshold.
 * It iteratively reduces the compression quality until the image size is below the threshold.
 * The compressed image is then written back to the original file.
 *
 * @return A File object representing the reduced image file.
 */
fun File.reduceFileImage(): File {
    // Initialize variables
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int

    // Compress the image until its size is within the threshold
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5 // Reduce compression quality
    } while (streamLength > MAXIMAL_SIZE)

    // Write the compressed image back to the file
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

    return file
}