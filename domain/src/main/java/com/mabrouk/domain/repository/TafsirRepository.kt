package com.mabrouk.domain.repository

import com.mabrouk.domain.models.TafsirAya
import com.mabrouk.domain.response.TafsirResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface TafsirRepository {
    suspend fun requestTafsir(chapter_id:Int , verse_id:Int,id:Int) : Flow<Result<TafsirAya>>
    suspend fun saveTafsir(data:TafsirAya)
}