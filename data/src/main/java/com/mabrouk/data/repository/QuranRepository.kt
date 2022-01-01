package com.mabrouk.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.mabrouk.data.api.QuranApi
import com.mabrouk.data.db.quran.QuranDao
import com.mabrouk.data.entities.*
import com.mabrouk.data.utils.executeCall
import com.mabrouk.data.utils.toEntity
import com.mabrouk.domain.models.Juz
import com.mabrouk.domain.models.Sura
import com.mabrouk.domain.models.Verse
import com.mabrouk.domain.response.JuzResponse
import com.mabrouk.domain.response.SurasResponse
import com.mabrouk.domain.response.VersesResponse
import com.mabrouk.domain.utils.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class QuranRepository @Inject constructor(@ApplicationContext val context: Context, val api:QuranApi, val dao: QuranDao) : QuranDefultRepository {

    override suspend fun requestSuras(): Flow<Result<SurasResponse>> =
        executeCall(context) { api.getAllSura() }

    override suspend fun saveSuras(suras: ArrayList<Sura>) {
        dao.saveSuras(suras = suras.toEntity())
    }

    override suspend fun requestVerses(chapter_id: Int,page:Int): Flow<Result<VersesResponse>> =
            executeCall(context) { api.getSuraVerses(chapter_id,page) }

    override suspend fun saveVerses(verses: ArrayList<Verse>) {
        dao.saveVerses(verses.toEntity())
    }

    override suspend fun requestJuz(): Flow<Result<JuzResponse>> =
            executeCall(context) { api.getJuzs() }

    override suspend fun saveJuzs(juzs: ArrayList<Juz>) {
        dao.saveJuzs(juzs.toEntity())
    }

    fun getSurah(id:Int) = dao.getSuraById(id)

    override fun getSavedSuras() : LiveData<List<SuraEntity>> = dao.getSavedSuras()
    override fun getSavedVerses(chapter_id:Int) :Flow<List<VerseEntity>> = dao.getSaveVerses(chapter_id)
    override fun getSavedJuzs() : LiveData<List<JuzEntity>> = dao.getSavedJuzs()
    override fun getSavedTafsir(chapter_id: Int,verse_id: Int) : Flow<List<TafsirAyaEntity>> = dao.getSavedTafsir("/quran/$chapter_id/$verse_id/")
    override suspend fun updateJuz(juz:JuzEntity) {
        dao.updateJUz(juz)
    }
    override suspend fun updateSura(sura:SuraEntity){
        dao.updateSura(sura)
    }

    override suspend fun insertReaders(readers: ArrayList<QuranReaderEntity>) {
        dao.insertAllReaders(readers)
    }

    override suspend fun updateReader(readers: QuranReaderEntity) {
        dao.updateReader(readers)
    }

}