package com.mabrouk.domain.models

data class HadithBookNumber(
    var collectionName:String?,
    val bookNumber:String,
    val hadithStartNumber:Int,
    val hadithEndNumber:Int,
    val numberOfHadith:Int,
    val book:ArrayList<HadithBook>
)