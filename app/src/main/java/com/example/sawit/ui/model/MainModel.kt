package com.example.sawit.ui.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class MainModel(
    val writtenText: String = "",
    val distance: String = "",
    val estimatedTime: String = "",
): Parcelable