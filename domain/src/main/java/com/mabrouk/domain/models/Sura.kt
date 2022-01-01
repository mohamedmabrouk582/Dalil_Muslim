package com.mabrouk.domain.models


data class Sura(
    val id:Int,
    val bismillah_pre:Boolean=true,
    val revelation_place:String?=null,
    val name_complex:String?=null,
    val name_arabic:String?=null,
    val name_simple:String?=null,
    val verses_count:Int=0,
    var from_to:String?=null,
    var isDownload:Boolean=false
    )