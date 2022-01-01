package com.mabrouk.data.repository

import android.content.Context
import com.mabrouk.data.api.HadithApi
import com.mabrouk.data.db.hadith.HadithDao
import com.mabrouk.data.entities.HadithBookNumberEntity
import com.mabrouk.data.entities.HadithCategoryEntity
import com.mabrouk.data.entities.HadithEntity
import com.mabrouk.data.utils.executeCall
import com.mabrouk.data.utils.executeCall2
import com.mabrouk.data.utils.toEntity
import com.mabrouk.domain.models.HadithBookNumber
import com.mabrouk.domain.models.HadithCategory
import com.mabrouk.domain.models.HadithModel
import com.mabrouk.domain.repository.HadithDefaultRepository
import com.mabrouk.domain.response.HadithResponse
import com.mabrouk.domain.utils.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HadithRepository @Inject constructor(@ApplicationContext val context: Context, val api:HadithApi , val dao: HadithDao): HadithMyDafaultRepository {

    override suspend fun getHadithCategories(): Flow<Result<HadithResponse<ArrayList<HadithCategory>>>> {
        return executeCall(context){api.getHadithCategoriesAsync()}
    }

    override suspend fun savedHadithCategories(data: ArrayList<HadithCategory>) {
        dao.insertHadithCategories(data.toEntity())
    }

    override suspend fun saveHadithBook(data: ArrayList<HadithBookNumber>) {
        dao.insertHadithBookNumbers(data.toEntity())
    }

    override suspend fun saveHadith(data: ArrayList<HadithModel>) {
        dao.insertHadith(data = data.toEntity())
    }

    override suspend fun getHadithBookNumber(collectionName: String): Flow<Result<HadithResponse<ArrayList<HadithBookNumber>>>> {
        return executeCall(context){api.getHadithBooks(collectionName)}
    }

    override suspend fun getHadith(
        collectionName: String,
        bookNumber: String,page:Int
    ): Flow<Result<HadithResponse<ArrayList<HadithModel>>>> {
        return executeCall(context){api.getHadith(collectionName, bookNumber,page)}
    }


    override suspend fun getSavedHadithCategories() : Flow<List<HadithCategoryEntity>> =
        dao.getHadithCategories()

    override suspend fun getSavedHadithBooks(name: String): Flow<List<HadithBookNumberEntity>> {
        return dao.getHadithBooks(name)
    }

    override suspend fun getSavedHadith(
        name: String,
        bookNumber: String
    ): Flow<List<HadithEntity>> {
        return dao.getHadiths(name,bookNumber)
    }
}