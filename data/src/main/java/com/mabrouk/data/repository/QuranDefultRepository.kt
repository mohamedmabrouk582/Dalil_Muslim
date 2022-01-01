package com.mabrouk.data.repository

import androidx.lifecycle.LiveData
import com.mabrouk.data.entities.*
import com.mabrouk.data.utils.toEntity
import com.mabrouk.domain.models.Sura
import com.mabrouk.domain.repository.JuzRepository
import com.mabrouk.domain.repository.SurasRepository
import com.mabrouk.domain.repository.TafsirRepository
import com.mabrouk.domain.repository.VersesRepository
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
interface QuranDefultRepository  : SurasRepository, VersesRepository,
    JuzRepository {

    fun getSavedSuras() : LiveData<List<SuraEntity>>
    fun getSavedVerses(chapter_id:Int) : Flow<List<VerseEntity>>
    fun getSavedJuzs() : LiveData<List<JuzEntity>>
    fun getSavedTafsir(chapter_id: Int,verse_id: Int) : Flow<List<TafsirAyaEntity>>
    suspend fun updateJuz(juz: JuzEntity)
    suspend fun updateSura(sura: SuraEntity)
    suspend fun insertReaders(readers:ArrayList<QuranReaderEntity>)
    suspend fun updateReader(readers:QuranReaderEntity)
}