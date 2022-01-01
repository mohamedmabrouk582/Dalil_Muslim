package com.mabrouk.domain.repository

import com.mabrouk.domain.models.Juz
import com.mabrouk.domain.models.Sura
import com.mabrouk.domain.response.JuzResponse
import com.mabrouk.domain.response.SurasResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface JuzRepository {
    suspend fun requestJuz() : Flow<Result<JuzResponse>>
    suspend fun saveJuzs(suras:ArrayList<Juz>)
}