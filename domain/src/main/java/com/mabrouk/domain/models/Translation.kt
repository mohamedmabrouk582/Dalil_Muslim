package com.mabrouk.domain.models


data class Translation(
    val id:Long,
    val language_name:String?,
    val textTranslation:String?,
    val textIndopak:String?,
    val verseKey:String?,
    val audioUrl:String?,
    val textTransliteration:String?
)