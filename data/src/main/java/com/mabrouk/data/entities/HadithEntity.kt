package com.mabrouk.data.entities

import androidx.room.Entity
import com.mabrouk.domain.models.Hadith

@Entity(primaryKeys = ["collection","bookNumber","hadithNumber"])
data class HadithEntity(
    val collection:String,
    val bookNumber:String,
    val chapterId:String,
    val hadithNumber:String,
    val hadith:ArrayList<Hadith>
)