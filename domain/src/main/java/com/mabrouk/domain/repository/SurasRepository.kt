package com.mabrouk.domain.repository

import com.mabrouk.domain.models.Sura
import com.mabrouk.domain.response.SurasResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface SurasRepository {
    suspend fun requestSuras() : Flow<Result<SurasResponse>>
    suspend fun saveSuras(suras:ArrayList<Sura>)
}