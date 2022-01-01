package com.mabrouk.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mabrouk.domain.models.Sura
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class StoryEntity(
    @PrimaryKey
    val video_key:String ,
    var url:String,
    var title:String,
    var getThumbUrl:String,
    var suraId: Int? = 0,
    var videoId:Int = 0,
    var isPlaying:Boolean = false
) : Parcelable