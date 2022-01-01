package com.mabrouk.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaEntity(
        val url:String,
        val provider:String,
        val author_name:String
):Parcelable