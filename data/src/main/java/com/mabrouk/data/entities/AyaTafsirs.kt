package com.mabrouk.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AyaTafsirs(val name:String,val tafsirs:ArrayList<TafsirAyaEntity>):Parcelable