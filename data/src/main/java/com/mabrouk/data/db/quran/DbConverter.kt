package com.mabrouk.data.db.quran

import androidx.room.TypeConverter
import com.mabrouk.data.entities.MediaEntity
import com.mabrouk.data.entities.TranslationEntity
import com.mabrouk.domain.models.Translation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DbConverter {
   @TypeConverter
   fun fromMapToJson(data:Map<String,String>?) : String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToMap(data:String?) : Map<String,String>? {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(data,type)
    }

    @TypeConverter
    fun fromTranslationListToJson(data:ArrayList<TranslationEntity>?) :String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToTranslationList(data: String?): ArrayList<TranslationEntity>? {
        val type= object : TypeToken<ArrayList<TranslationEntity>>(){}.type
        return Gson().fromJson(data,type)
    }

    @TypeConverter
    fun fromMediaListToJson(data:ArrayList<MediaEntity>?) :String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToMediaList(data: String?): ArrayList<MediaEntity>? {
        val type= object : TypeToken<ArrayList<MediaEntity>>(){}.type
        return Gson().fromJson(data,type)
    }

    @TypeConverter
    fun fromLonListToJson(data:ArrayList<Long>?) :String? = Gson().toJson(data)

    @TypeConverter
    fun fromJsonToLongList(data: String?): ArrayList<Long>? {
        val type= object : TypeToken<ArrayList<Long>>(){}.type
        return Gson().fromJson(data,type)
    }

}