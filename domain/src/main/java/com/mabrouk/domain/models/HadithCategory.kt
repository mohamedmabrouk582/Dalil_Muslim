package com.mabrouk.domain.models

data class HadithCategory(
    val name:String,
    val hasBooks:Boolean,
    val hasChapters:Boolean,
    val totalHadith:Int,
    val totalAvailableHadith:Int,
    val collection:ArrayList<HadithCollection>
)