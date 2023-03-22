package com.example.sawit.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle

@Suppress("unused")
class SessionManager(private val context: Context) {

    companion object{
        private const val KEY_TOKEN = "key_token"
        private const val KEY_NAME = "key_name"
        private const val KEY_USERNAME = "key_username"
    }

    @SuppressLint("CommitPrefEdits")
    fun setSession(data: Bundle?) {
        data?.also {
            // set editor
            getSessionSharedPreference().edit().apply {
                putString(KEY_TOKEN, it.getString(KEY_TOKEN))
                putString(KEY_NAME, it.getString(KEY_NAME))
                putString(KEY_USERNAME, it.getString(KEY_USERNAME))
                apply()
            }
        }
    }

    /**
     * Clear shared preference
     */
    @SuppressLint("CommitPrefEdits")
    inline fun clearSession(callback: () -> Unit){
        getSessionSharedPreference().edit().clear().apply()
        callback()
    }

    fun getToken() = getSessionSharedPreference().getString(KEY_TOKEN, null)

    fun getName() = getSessionSharedPreference().getString(KEY_NAME, null)

    fun getUsername() = getSessionSharedPreference().getString(KEY_USERNAME, null)

    fun isExist() = getToken().let { it != null && it.isNotEmpty() }

    fun getSessionSharedPreference(): SharedPreferences =
        context.getSharedPreferences(GlobalConfig.SHARED_PREFERENCE_SESSION, Context.MODE_PRIVATE)
}