package com.mabrouk.data.db.hadith

import androidx.room.*
import com.mabrouk.data.entities.HadithBookNumberEntity
import com.mabrouk.data.entities.HadithCategoryEntity
import com.mabrouk.data.entities.HadithEntity
import com.mabrouk.domain.models.HadithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface HadithDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHadithCategories(categories:ArrayList<HadithCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHadithBookNumbers(books:ArrayList<HadithBookNumberEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHadith(data:ArrayList<HadithEntity>)


    @Query("select * from HadithCategoryEntity")
    fun getHadithCategories() : Flow<List<HadithCategoryEntity>>

    @Query("select * from HadithBookNumberEntity where collectionName =:name")
    fun getHadithBooks(name:String) : Flow<List<HadithBookNumberEntity>>

    @Query("Select * from HadithEntity where collection =:collection and bookNumber=:bookNumber")
    fun getHadiths(collection:String,bookNumber:String) : Flow<List<HadithEntity>>


}