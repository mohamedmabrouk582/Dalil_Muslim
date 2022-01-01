package com.mabrouk.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParaceVerses(
    val suraEntity: SuraEntity,
    val verses:ArrayList<VerseEntity>) : Parcelable