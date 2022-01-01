package com.mabrouk.data.repository

import android.content.Context
import com.mabrouk.data.api.QuranApi
import com.mabrouk.data.db.quran.QuranDao
import com.mabrouk.data.entities.RadioEntity
import com.mabrouk.data.utils.executeCall
import com.mabrouk.data.utils.toEntity
import com.mabrouk.domain.models.Radio
import com.mabrouk.domain.repository.RadioRepository
import com.mabrouk.domain.response.RadioResponse
import com.mabrouk.domain.utils.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 06/09/2021 created by Just clean
 */
class RadioRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val api: QuranApi,
    val dao: QuranDao
) : RadioDefaultRepository {

    override suspend fun requestRadios(): Flow<Result<RadioResponse>> =
      executeCall(context){api.getRadios("http://api.mp3quran.net/radios/radio_arabic.json")}


    override suspend fun insertRadios(items: ArrayList<Radio>) {
        dao.insertAllRadios(items.toEntity())
    }

    override suspend fun getSavedRadios() : ArrayList<RadioEntity>{
        return  ArrayList(dao.getRadios().first())
    }
}