package com.mabrouk.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "azkar")
data class AzkarEntity(
        @ColumnInfo(name = "category")
        val cetegory:String?,
        val zekr:String?,
        val description:String?,
        val count:Int?=1,
        val reference:String?,
        @PrimaryKey
        val id:Int?
)