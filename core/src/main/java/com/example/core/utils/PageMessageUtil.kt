package com.example.core.utils

import android.content.Context
import android.view.View
import com.example.core.utils.ext.showSnackBarClose
import com.example.core.utils.ext.showToast

/**
 * utility to show message base of type message
 */
class PageMessageUtil(
    private val context: Context,
    private val view: View,
    private val buttonActionTextColor: Int? = null
) {

    enum class Type{
        TOAST, SNACK_BAR
    }

    /**
     * show message base of type message
     */
    fun showMessage(type: Type, msg: String){
        when(type){
            Type.SNACK_BAR -> view.showSnackBarClose(msg = msg, buttonActionTextColor = buttonActionTextColor)
            else -> context.showToast(msg)
        }
    }
}