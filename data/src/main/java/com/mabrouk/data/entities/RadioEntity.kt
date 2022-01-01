package com.mabrouk.data.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 06/09/2021 created by Just clean
 */
@Entity
data class RadioEntity(
    @NonNull
    @PrimaryKey
    val name:String,
    val radioUrl : String
)