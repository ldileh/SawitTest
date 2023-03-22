package com.example.core.component.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.core.R
import com.example.core.utils.ext.logError
import java.lang.Exception

class CustomProgressDialog(context: Context) : Dialog(context) {

    init {
        // dialog configuration
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
    }

    fun showDialog(isShow: Boolean) {
        try {
            if (isShow){
                if (!isShowing) show()
            }else{
                if (isShowing) dismiss()
            }

        }catch (e: Exception){
            logError(e.message ?: "error while ${if (isShow) "show" else "hide"} dialog")
        }
    }
}