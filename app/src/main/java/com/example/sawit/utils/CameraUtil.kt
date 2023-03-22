package com.example.sawit.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.core.utils.ext.showToast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
fun createImageFile(context: Context, onResult: (currentPhotoPath: String) -> Unit): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Save a file: path for use with ACTION_VIEW intents
        onResult(absolutePath)
    }
}

fun dispatchTakePictureIntent(context: Activity, requestCode: Int, onResult: (currentPhotoPath: String) -> Unit) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        try {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile(context, onResult)
            } catch (e: IOException) {
                // Error occurred while creating the File
                context.showToast(e.localizedMessage)
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    context,
                    "com.example.sawit.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                context.startActivityForResult(takePictureIntent, requestCode)
            }
        } catch (e: ActivityNotFoundException) {
            context.showToast(e.localizedMessage ?: "Cannot take picture")
        }
    }
}