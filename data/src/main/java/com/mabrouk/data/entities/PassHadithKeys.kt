package com.mabrouk.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PassHadithKeys(
    val name:String,
    val bookNumber:String
) : Parcelable