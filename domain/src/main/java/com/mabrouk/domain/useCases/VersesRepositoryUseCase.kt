package com.mabrouk.domain.useCases

import com.mabrouk.domain.models.Verse
import com.mabrouk.domain.repository.VersesRepository
import com.mabrouk.domain.response.VersesResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class VersesRepositoryUseCase(val repository: VersesRepository) {

    suspend fun requestVerses(chapter_id:Int,page:Int) : Flow<Result<VersesResponse>> =
        repository.requestVerses(chapter_id,page)
    suspend fun saveVerses(verses:ArrayList<Verse>){

    }

}