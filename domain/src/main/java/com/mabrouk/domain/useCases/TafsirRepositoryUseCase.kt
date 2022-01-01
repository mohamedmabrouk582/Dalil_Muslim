package com.mabrouk.domain.useCases

import com.mabrouk.domain.models.TafsirAya
import com.mabrouk.domain.repository.TafsirRepository
import com.mabrouk.domain.response.TafsirResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class TafsirRepositoryUseCase(val  repository: TafsirRepository) {
    suspend fun requestTafsir(chapter_id:Int , verse_id:Int,id:Int) : Flow<Result<TafsirAya>> =
        repository.requestTafsir(chapter_id, verse_id,id)

    suspend fun saveTafsir(data:TafsirAya) {
        repository.saveTafsir(data)
    }
}