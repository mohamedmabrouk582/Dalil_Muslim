package com.mabrouk.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TranslationEntity(
    val id:Long,
    val language_name:String?,
    val textTranslation:String?,
    val textIndopak:String?,
    val verseKey:String?,
    val audioUrl:String?,
    val textTransliteration:String?
): Parcelable