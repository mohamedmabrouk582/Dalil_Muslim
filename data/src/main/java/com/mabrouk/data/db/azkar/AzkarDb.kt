package com.mabrouk.data.db.azkar

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mabrouk.data.entities.AzkarEntity

@Database(entities = [AzkarEntity::class],version = 1,exportSchema = false)
abstract class AzkarDb : RoomDatabase(){
    abstract fun getAzkarDao() : AzkarDao
}