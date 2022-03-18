package com.mabrouk.data.db.quran

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mabrouk.data.entities.*
import com.mabrouk.domain.models.Juz
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRadios(radios:ArrayList<RadioEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReaders(readers : ArrayList<QuranReaderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSuras(suras:ArrayList<SuraEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJuzs(juzs:ArrayList<JuzEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVerses(vers:ArrayList<VerseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVerseTafsirs(tafsirEntities:ArrayList<TafsirAyaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVerseTafsir(tafsirEntities:TafsirAyaEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateJUz(juz:JuzEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateVerse(Verse:VerseEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReader(readerEntity: QuranReaderEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSura(sura:SuraEntity)

    @Query("select * from radioentity")
    fun getRadios() : Flow<List<RadioEntity>>

    @Query("select * from QuranReaderEntity")
    fun getAllReaders () : Flow<List<QuranReaderEntity>>

    @Query("select * from QuranReaderEntity where readerId =:id")
    fun getReader(id:Int) : Flow<QuranReaderEntity>

    @Query("select * from suraentity where sura_id=:id")
    fun getSuraById(id:Int) : Flow<SuraEntity>

    @Query("select * from suraentity")
    fun getSavedSuras() : LiveData<List<SuraEntity>>

    @Query("select * from juzentity")
    fun getSavedJuzs() : LiveData<List<JuzEntity>>

    @Query("select * from verseentity where chapter_id =:id")
    fun getSaveVerses(id:Int) : Flow<List<VerseEntity>>

    @Query("select * from tafsirayaentity where verse_key=:key ")
    fun getSavedTafsir(key:String) : Flow<List<TafsirAyaEntity>>


    @Query("select * from SuraEntity where name_arabic LIKE '%' || :search || '%' ")
    fun searchByAtSurah(search:String) : Flow<List<SuraEntity>>



}