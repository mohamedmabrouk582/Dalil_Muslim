package com.mabrouk.data.entities

import com.mabrouk.domain.models.Sura

data class Juz_Sura(
    val juz_num:Int,
    val verse_ids:List<Int>,
    val verse_map:Map<String,String>?,
    val sura:SuraEntity?=null,
    var from_to:String?=null,
    var isDownload:Boolean=false
)