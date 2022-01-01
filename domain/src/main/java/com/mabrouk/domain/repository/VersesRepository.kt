package com.mabrouk.domain.repository

import com.mabrouk.domain.models.Verse
import com.mabrouk.domain.response.VersesResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface VersesRepository {
    suspend fun requestVerses(chapter_id:Int,page:Int=0) : Flow<Result<VersesResponse>>
    suspend fun saveVerses(verses:ArrayList<Verse>)
}