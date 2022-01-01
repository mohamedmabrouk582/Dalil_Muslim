package com.mabrouk.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class TafsirAyaEntity(
    val tafsir_id:Int,
    val verse_id:Int,
    @PrimaryKey
    val t_text:String="",
    val t_language_name:String?=null,
    val t_resource_name:String?=null,
    val verse_key:String="0:0",
    val chapter_id:Int=0
) : Parcelable
