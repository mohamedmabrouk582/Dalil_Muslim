package com.mabrouk.domain.useCases

import com.mabrouk.domain.models.Sura
import com.mabrouk.domain.repository.SurasRepository
import com.mabrouk.domain.response.SurasResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class SurasRepositoryUseCase(val repository:SurasRepository) {
    suspend fun requestSuras() : Flow<Result<SurasResponse>> =
        repository.requestSuras()
    suspend fun saveSuras(suras:ArrayList<Sura>){
        repository.saveSuras(suras)
    }
}