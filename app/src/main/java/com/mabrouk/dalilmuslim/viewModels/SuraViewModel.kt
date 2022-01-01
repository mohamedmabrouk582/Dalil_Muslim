package com.mabrouk.dalilmuslim.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.mabrouk.dalilmuslim.states.StoryStates
import com.mabrouk.dalilmuslim.states.SurahStates
import com.mabrouk.dalilmuslim.utils.JUZ_LIST_DOWNLOADS
import com.mabrouk.dalilmuslim.utils.READERS_DOWNLOADS
import com.mabrouk.dalilmuslim.utils.VERSES_IDS
import com.mabrouk.dalilmuslim.viewModels.base.BaseViewModel
import com.mabrouk.dalilmuslim.workManger.SuraDownload
import com.mabrouk.data.entities.*
import com.mabrouk.data.repository.QuranDefultRepository
import com.mabrouk.data.utils.DataStorePreferences
import com.mabrouk.data.utils.toEntity
import com.mabrouk.domain.models.Juz
import com.mabrouk.domain.utils.Result
import com.google.common.reflect.TypeToken
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuraViewModel @Inject constructor(
    application: Application,
    val repository: QuranDefultRepository,
    val remoteConfig: FirebaseRemoteConfig,
    val  dataStore : DataStorePreferences
) : BaseViewModel(application) {
    var juzsResult = MutableLiveData<ArrayList<Juz>>()
    private val _states = MutableStateFlow<SurahStates>(SurahStates.Idle)
    val states: StateFlow<SurahStates> = _states

    fun requestJuz() {
        viewModelScope.launch {
            repository.requestJuz().collect {
                when (it) {
                    is Result.OnLoading -> {
                        loader.set(it.loading)
                        error.set(null)
                    }
                    is Result.OnSuccess -> {
                        juzsResult.postValue(it.data.juzs)
                        _states.value = SurahStates.RequestJuzs(it.data.juzs.toEntity())
                        repository.saveJuzs(it.data.juzs)
                    }
                    is Result.OnFailure -> {
                        loader.set(false)
                        error.set(it.throwable.message!!)
                    }
                    is Result.NoInternetConnect -> {
                        loader.set(false)
                        error.set(it.error)
                    }

                }
            }
        }
    }

    override fun retry() {
        super.retry()
        requestSuras()
        requestJuz()
    }

    fun requestSuras() {
        viewModelScope.launch {
            repository.requestSuras().collect {
                when (it) {
                    is Result.OnLoading -> {
                        loader.set(it.loading)
                        error.set(null)
                    }
                    is Result.OnSuccess -> {
                        _states.value = SurahStates.RequestSurahs(it.data.chapters.toEntity())
                        repository.saveSuras(it.data.chapters)
                    }
                    is Result.OnFailure -> {
                        loader.set(false)
                        error.set(it.throwable.message!!)
                    }
                    is Result.NoInternetConnect -> {
                        loader.set(false)
                        error.set(it.error)
                    }
                }
            }
        }
    }

    suspend fun updateJuz(juz: JuzEntity) {
        repository.updateJuz(juz)
    }

    suspend fun updateSura(sura: SuraEntity) {
        repository.updateSura(sura)
    }

    fun downloadJuz(ids: List<Int>, context: Context): LiveData<WorkInfo> {
        val putIntArray =
            Data.Builder().putIntArray(VERSES_IDS, ids.map { it }.toIntArray())
        val build = OneTimeWorkRequest.Builder(SuraDownload::class.java)
            .setInputData(putIntArray.build())
            .build()

        val instance = WorkManager.getInstance(context)
        instance.enqueue(build)
        return instance.getWorkInfoByIdLiveData(build.id)
    }

    fun fetchVerse(id: Int): Flow<List<VerseEntity>> {
        return repository.getSavedVerses(id)
    }

    fun getSavedJuzs(): LiveData<List<JuzEntity>> {
        return repository.getSavedJuzs()
    }

    fun getSavedSuras(): LiveData<List<SuraEntity>> {
        return repository.getSavedSuras()
    }

    fun resetStates() {
        _states.value = SurahStates.Idle
    }



    fun getReaders(){
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val type = object : TypeToken<ArrayList<QuranReaderEntity>>() {}.type
                    val tt = remoteConfig.getString("quran_readers")
                    val data: ArrayList<QuranReaderEntity> = Gson().fromJson(
                        tt, type
                    )
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.insertReaders(data)
                        dataStore.setBoolean(READERS_DOWNLOADS,true)
                    }
                }

            }
    }

}