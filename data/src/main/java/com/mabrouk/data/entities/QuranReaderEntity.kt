package com.mabrouk.data.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 28/08/2021 created by Just clean
 */
@Entity
data class QuranReaderEntity(
    @PrimaryKey
    @SerializedName("id")
    val readerId: Int,
    val name: String,
    val sufix: String,
    var versesIds: ArrayList<Long>? = arrayListOf()
) {
    @Ignore
    var isSelected: Boolean = false
}