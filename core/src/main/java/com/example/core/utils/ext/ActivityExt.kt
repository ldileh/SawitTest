package com.example.core.utils.ext

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
@Suppress("unused")
fun Activity.forceCloseApp(){
    showToast("Close app")

    try {
        finishAffinity()
    }catch (e: Exception){
        logError(msg = e.message, e = e)
    }
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
@Suppress("unused")
fun Context.forceCloseApp(){
    if (this is Activity){
        forceCloseApp()
    }
}

@Suppress("unused")
fun Context.showToast(msg: String?) = try {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}catch (e: Exception){
    logError(msg = e.message, e = e)
}
