package com.mabrouk.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JuzEntity(
    @PrimaryKey
    val id:Int,
    val juz_number:Int,
    val verse_mapping:Map<String,String>?=null,
    var isDownload:Boolean=false
)