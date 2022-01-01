package com.mabrouk.domain.models

data class HadithModel(
    val collection:String,
    val bookNumber:String,
    val chapterId:String,
    val hadithNumber:String,
    val hadith:ArrayList<Hadith>
)