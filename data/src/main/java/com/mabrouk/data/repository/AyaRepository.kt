package com.mabrouk.data.repository

import android.content.Context
import com.mabrouk.data.api.QuranApi
import com.mabrouk.data.api.TafseerApi
import com.mabrouk.data.db.quran.QuranDao
import com.mabrouk.data.entities.QuranReaderEntity
import com.mabrouk.data.entities.SuraEntity
import com.mabrouk.data.entities.TafsirAyaEntity
import com.mabrouk.data.utils.executeCall
import com.mabrouk.data.utils.executeCall2
import com.mabrouk.data.utils.toEntity
import com.mabrouk.domain.models.TafsirAya
import com.mabrouk.domain.repository.TafsirRepository
import com.mabrouk.domain.utils.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.ResponseBody
import javax.inject.Inject

class AyaRepository @Inject constructor(@ApplicationContext val context: Context, val api : QuranApi,val tafseerApi:TafseerApi,val dao: QuranDao)  : TafsirRepository {
    suspend fun downloadAudio(url:String) : Flow<Result<ResponseBody>>{
        return executeCall(context){
            api.downloadAudio(url)
        }
    }

    fun getSurah(id:Int) = dao.getSuraById(id)

    suspend fun updateSura(sura: SuraEntity){
        dao.updateSura(sura)
    }

    fun getSavedTafsirs(chapter_id: Int,verse_id: Int) : Flow<List<TafsirAyaEntity>> {
        return dao.getSavedTafsir("/quran/$chapter_id/$verse_id/")
    }

    override suspend fun requestTafsir(chapter_id: Int, verse_id: Int,id:Int): Flow<Result<TafsirAya>> {
         return executeCall2(context){
             tafseerApi.getAyaTafseer(id,chapter_id,verse_id)
         }
    }

    override suspend fun saveTafsir(data: TafsirAya) {
        dao.saveVerseTafsir(data.toEntity())
    }

    suspend fun updateQuranReader(item:QuranReaderEntity){
        dao.updateReader(item)
    }

    suspend fun getReader(id:Int):Flow<QuranReaderEntity>{
        return dao.getReader(id)
    }

    suspend fun getReaders() : ArrayList<QuranReaderEntity>{
        return ArrayList(dao.getAllReaders().first())
    }

}