package com.mabrouk.data.repository

import com.mabrouk.data.entities.RadioEntity
import com.mabrouk.domain.repository.RadioRepository

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 06/09/2021 created by Just clean
 */
interface RadioDefaultRepository : RadioRepository {
    suspend fun getSavedRadios() : ArrayList<RadioEntity>
}