package com.mabrouk.dalilmuslim.viewModels

import android.app.Application
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mabrouk.dalilmuslim.states.VerseStates
import com.mabrouk.dalilmuslim.utils.*
import com.mabrouk.dalilmuslim.viewModels.base.BaseViewModel
import com.mabrouk.dalilmuslim.workManger.AudioDownloader
import com.mabrouk.dalilmuslim.workManger.MediaPlayerWorker
import com.mabrouk.data.entities.*
import com.mabrouk.data.repository.AyaRepository
import com.mabrouk.data.utils.DataStorePreferences
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VerseViewModel @Inject constructor(
    app: Application,
    val ayaRepository: AyaRepository,
    val dataStorePreferences: DataStorePreferences
) :
    BaseViewModel(app) {
    private val _states = MutableStateFlow<VerseStates>(VerseStates.Idle)
    val states: StateFlow<VerseStates> = _states
    val workManger by lazy { WorkManager.getInstance(getApplication()) }
    lateinit var currentReader: QuranReaderEntity

    init {
        viewModelScope.launch {
            currentReader = dataStorePreferences.getReader()
        }
    }

    fun downloadVerseAudio(surahname: String, verses: ArrayList<VerseEntity>) {
        val workManger = WorkManager.getInstance(getApplication())
        val map = verses.map {
            AudioDataPass(
                it.aya_id,
                currentReader.sufix,
                it.chapter_id,
                it.verse_number,
                surahname
            )
        }
        val data = Data.Builder().putString(SURA_LIST_AUDIOS, Gson().toJson(ArrayList(map))).build()
        val request = OneTimeWorkRequest.Builder(AudioDownloader::class.java)
            .setInputData(data)
            .build()
        workManger.enqueue(request)
        _states.value = VerseStates.DownloadVerse(workManger.getWorkInfoByIdLiveData(request.id))
    }

    fun getTafsier(id: Int, verse_id: Int): Flow<List<TafsirAyaEntity>> =
        ayaRepository.getSavedTafsirs(id, verse_id)


    fun updateSura(sura: SuraEntity) = viewModelScope.launch { ayaRepository.updateSura(sura) }

    fun youtube(url: String?) {
        url?.apply {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            getApplication<Application>().startActivity(webIntent)
        }
    }

    fun resetStates() {
        _states.value = VerseStates.Idle
    }

    override fun onCleared() {
        workManger.cancelAllWork()
        super.onCleared()
    }

    fun getReader(reader: (item: QuranReaderEntity) -> Unit) {
        viewModelScope.launch {
            var id = 1
            try {
                id = currentReader.readerId
            } catch (e: Exception) {
                currentReader = dataStorePreferences.getReader()
                id = currentReader.readerId

            } finally {
                ayaRepository.getReader(id).first().apply {
                    reader(this)
                }
            }

        }
    }

    fun getAllReaders(readers: (data: ArrayList<QuranReaderEntity>) -> Unit) {
        viewModelScope.launch {
            readers(ayaRepository.getReaders())
        }
    }


    fun updateReader(reader: QuranReaderEntity) {
        currentReader = reader
        viewModelScope.launch {
            dataStorePreferences.setReader(currentReader)
        }
        _states.value = VerseStates.UpdateReader(currentReader)
    }


    fun updateReader(suraId: Long) {
        Log.d("downloadVerse", currentReader.toString())
        viewModelScope.launch(Dispatchers.IO) {
            ayaRepository.updateQuranReader(currentReader.let {
                if (it.versesIds == null) it.versesIds = arrayListOf()
                it.versesIds?.add(suraId)
                it
            })
            dataStorePreferences.setReader(currentReader)
        }
    }

}