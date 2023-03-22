package com.example.core.utils.ext

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

@Suppress("unused")
fun View.showSnackBar(msg: String?) {
    snackBarGenerator(msg).show()
}

@Suppress("unused")
fun View.showSnackBar(
    msg: String?,
    buttonActionText: String,
    buttonActionCallback: () -> Unit,
) {
    snackBarGenerator(msg)
        .setAction(buttonActionText) { buttonActionCallback() }
        .show()
}

@Suppress("unused")
fun View.showSnackBarClose(
    msg: String?,
    buttonActionText: String = "Dismiss",
    buttonActionTextColor: Int? = null,
) {
    snackBarGenerator(msg).apply {
        setAction(buttonActionText) { dismiss() }

        if(buttonActionTextColor != null){
            setActionTextColor(ContextCompat.getColor(context, buttonActionTextColor))
        }

        show()
    }
}

fun View.snackBarGenerator(msg: String?) = Snackbar
    .make(this, msg ?: "", Snackbar.LENGTH_SHORT)
    .setTextColor(Color.WHITE)