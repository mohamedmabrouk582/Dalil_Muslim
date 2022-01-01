package com.mabrouk.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mabrouk.domain.models.HadithCollection
@Entity
data class HadithCategoryEntity (
    @PrimaryKey
    val name:String,
    val hasBooks:Boolean,
    val hasChapters:Boolean,
    val totalHadith:Int,
    val totalAvailableHadith:Int,
    val collection:ArrayList<HadithCollection>
    )