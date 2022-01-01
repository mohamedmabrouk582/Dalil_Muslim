package com.mabrouk.data.db.azkar

import androidx.room.Dao
import androidx.room.Query
import com.mabrouk.data.entities.AzkarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AzkarDao {
    @Query("select * from azkar where category =:category")
    fun getAzkarBYCategory(category:String) : Flow<List<AzkarEntity>>

    @Query("select category from azkar")
    fun getAzkarCategories() : Flow<List<String>>
}