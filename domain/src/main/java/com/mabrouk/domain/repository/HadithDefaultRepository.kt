package com.mabrouk.domain.repository

import com.mabrouk.domain.models.HadithBookNumber
import com.mabrouk.domain.models.HadithCategory
import com.mabrouk.domain.models.HadithModel
import com.mabrouk.domain.response.HadithResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow


interface HadithDefaultRepository {
    suspend fun getHadithCategories() : Flow<Result<HadithResponse<ArrayList<HadithCategory>>>>
    suspend fun savedHadithCategories(data:ArrayList<HadithCategory>)
    suspend fun saveHadithBook(data: ArrayList<HadithBookNumber>)
    suspend fun saveHadith(data:ArrayList<HadithModel>)
    suspend fun getHadithBookNumber(collectionName:String) : Flow<Result<HadithResponse<ArrayList<HadithBookNumber>>>>
    suspend fun getHadith(collectionName:String,bookNumber:String,page:Int) : Flow<Result<HadithResponse<ArrayList<HadithModel>>>>

}