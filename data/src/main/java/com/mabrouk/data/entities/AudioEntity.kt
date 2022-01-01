package com.mabrouk.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AudioEntity(
    val url:String,
    val duration:Int,
    val format:String
): Parcelable
