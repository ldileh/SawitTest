package com.example.sawit.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun getTessDataPath(context: Context): String? {
    // We need to return folder that contains the "tessdata" folder,
    // which is in this sample directly the app's files dir
    return context.filesDir.absolutePath
}

fun extractAssets(context: Context) {
    val am: AssetManager = context.assets
    val tessDir = File(getTessDataPath(context), "tessdata")
    if (!tessDir.exists()) {
        tessDir.mkdir()
    }
    val engFile = File(tessDir, "eng.traineddata")
    if (!engFile.exists()) {
        copyFile(am, "eng.traineddata", engFile)
    }
}

private fun copyFile(
    am: AssetManager, assetName: String,
    outFile: File
) {
    try {
        am.open(assetName).use { `in` ->
            FileOutputStream(outFile).use { out ->
                val buffer = ByteArray(1024)
                var read: Int
                while (`in`.read(buffer).also { read = it } != -1) {
                    out.write(buffer, 0, read)
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}