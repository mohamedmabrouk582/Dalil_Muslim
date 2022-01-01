package com.mabrouk.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mabrouk.domain.models.HadithBook

@Entity(primaryKeys = ["collectionName","bookNumber"])
data class HadithBookNumberEntity(
    var collectionName:String,
    val bookNumber:String,
    val hadithStartNumber:Int,
    val hadithEndNumber:Int,
    val numberOfHadith:Int,
    val book:ArrayList<HadithBook>
)