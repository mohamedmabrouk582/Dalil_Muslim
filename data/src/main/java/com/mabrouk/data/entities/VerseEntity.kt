package com.mabrouk.data.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class VerseEntity(
    @PrimaryKey
    val aya_id:Int,
    val verse_number:Int,
    val chapter_id:Int,
    val text_madani:String?=null,
    val text_indopak:String?=null,
    val text_simple:String?=null,
    val juz_number:Int?=null,
    val hizb_number:Int?=null,
    val rub_number:Int?=null,
    val sajdah:String?=null,
    val sajdah_number:Int?=null,
    val page_number:Int?=null,
    @Embedded
    val audio: AudioEntity?=null,
    val translations:ArrayList<TranslationEntity>?=null,
    val media_contents:ArrayList<MediaEntity>?=null,
    var selected:Boolean=false
) : Parcelable