package com.mabrouk.domain.repository

import com.mabrouk.domain.models.Radio
import com.mabrouk.domain.response.RadioResponse
import com.mabrouk.domain.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 06/09/2021 created by Just clean
 */
interface RadioRepository {
    suspend fun requestRadios() : Flow<Result<RadioResponse>>
    suspend fun insertRadios(items : ArrayList<Radio>)
}