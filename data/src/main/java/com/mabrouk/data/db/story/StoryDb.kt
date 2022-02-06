package com.mabrouk.data.db.story

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mabrouk.data.entities.StoryEntity

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 27/08/2021 created by Just clean
 */
@Database(entities = [StoryEntity::class], version = 3 , exportSchema = false)
abstract class StoryDb : RoomDatabase(){
    abstract fun getDao() : StoryDao
}