package com.mabrouk.data.repository

import com.mabrouk.data.entities.HadithBookNumberEntity
import com.mabrouk.data.entities.HadithCategoryEntity
import com.mabrouk.data.entities.HadithEntity
import com.mabrouk.domain.models.HadithModel
import com.mabrouk.domain.repository.HadithDefaultRepository
import kotlinx.coroutines.flow.Flow

interface HadithMyDafaultRepository : HadithDefaultRepository {
    suspend fun getSavedHadithCategories() : Flow<List<HadithCategoryEntity>>
    suspend fun getSavedHadithBooks(name:String) : Flow<List<HadithBookNumberEntity>>
    suspend fun getSavedHadith(name:String,bookNumber:String) : Flow<List<HadithEntity>>
}