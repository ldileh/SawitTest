package com.example.core.utils.ext

import timber.log.Timber
import java.lang.Exception

@Suppress("unused")
fun logError(msg: String?, e: Exception? = null){
    if (e != null) Timber.e(e, msg) else Timber.e(msg)
}

@Suppress("unused")
fun logDebug(msg: String?, e: Exception? = null){
    if (e != null) Timber.d(e, msg) else Timber.d(msg)
}

@Suppress("unused")
fun logWarning(msg: String?, e: Exception? = null){
    if (e != null) Timber.w(e, msg) else Timber.w(msg)
}