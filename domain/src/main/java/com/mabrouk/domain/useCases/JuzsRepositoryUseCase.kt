package com.mabrouk.domain.useCases


import com.mabrouk.domain.repository.JuzRepository
import com.mabrouk.domain.models.Juz
import com.mabrouk.domain.response.JuzResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class JuzsRepositoryUseCase (val repository: JuzRepository) {
    suspend fun requestJuzs(): Flow<Result<JuzResponse>> =
        repository.requestJuz()

    suspend fun saveJuzs(juzs: ArrayList<Juz>) {
        repository.saveJuzs(juzs)
    }
}