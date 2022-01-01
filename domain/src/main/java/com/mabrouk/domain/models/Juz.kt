package com.mabrouk.domain.models

data class Juz(
    val id:Int,
    val juz_number:Int,
    val verse_mapping:Map<String,String>?=null,
    var isDownload:Boolean=false
)