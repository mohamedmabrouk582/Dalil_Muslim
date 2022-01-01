package com.mabrouk.domain.models

import com.google.gson.annotations.SerializedName


data class TafsirAya(
    @SerializedName("tafseer_id")
    val id:Int,
    @SerializedName("ayah_number")
    val verse_id:Int,
    val text:String?=null,
    val language_name:String?=null,
    @SerializedName("tafseer_name")
    val resource_name:String?=null,
    @SerializedName("ayah_url")
    val verse_key:String="/quran/1/1/"
){
    var chapter_id:Int = 0
        set(value) {
        field=verse_key.replace("/quran/","").split("/")[0].toInt()
    }
}