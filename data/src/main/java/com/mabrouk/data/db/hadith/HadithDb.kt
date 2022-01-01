package com.mabrouk.data.db.hadith

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mabrouk.data.entities.HadithBookNumberEntity
import com.mabrouk.data.entities.HadithCategoryEntity
import com.mabrouk.data.entities.HadithEntity

@Database(entities = [HadithCategoryEntity::class,HadithBookNumberEntity::class,HadithEntity::class] , version = 1 , exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class HadithDb : RoomDatabase(){
    abstract fun getHadithDao() : HadithDao
}