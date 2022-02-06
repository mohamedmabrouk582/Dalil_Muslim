package com.mabrouk.dalilmuslim.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.states.StoryStates
import com.mabrouk.dalilmuslim.utils.Notification
import com.mabrouk.dalilmuslim.viewModels.base.BaseViewModel
import com.mabrouk.data.entities.StoryEntity
import com.mabrouk.data.repository.StoryDefaultRepository
import com.mabrouk.data.utils.CheckNetwork
import com.google.android.exoplayer2.MediaItem
import com.google.common.reflect.TypeToken
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.mabrouk.dalilmuslim.utils.VIDEO_KEY
import com.mabrouk.dalilmuslim.workManger.VideoDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor(
    app: Application,
    val repository: StoryDefaultRepository,
    val remoteConfig: FirebaseRemoteConfig
) : BaseViewModel(app) {

    private val _states = MutableStateFlow<StoryStates>(StoryStates.Idle)
    val states: StateFlow<StoryStates> = _states

    fun loadStories() {
        if (CheckNetwork.isOnline(getApplication())){
            getRemoteData()
        }else{
            Toast.makeText(getApplication(),getApplication<Application>().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
//            viewModelScope.launch {
//               repository.getStories().first().forEach {
//                   _states.value = StoryStates.AddStory(it)
//
//               }
//            }
        }
    }

    private fun getRemoteData(){
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val type = object : TypeToken<ArrayList<StoryEntity>>() {}.type
                    val tt = remoteConfig.getString("videos_ids")
                    val data: ArrayList<StoryEntity> = Gson().fromJson(
                        tt, type
                    )
                    viewModelScope.launch {
                       // val stories = repository.getStories().first()
                        data.forEach { story ->
//                            val item = stories.find { it.video_key == story.video_key }
//                            if (item!=null){
//                                _states.value = StoryStates.AddStory(item)
//                            }else{
                                getYoutubeUrl(story.video_key)
                          //  }
                        }
                    }

                    _states.value = StoryStates.ShowNotification
                }

            }
    }

    @SuppressLint("StaticFieldLeak")
    fun getYoutubeUrl(key: String) {
        object : YouTubeExtractor(getApplication()) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (ytFiles?.get(18)?.url!=null){
                    val story = StoryEntity(
                        key,
                        ytFiles.get(18)?.url?:"",
                        videoMeta?.title ?: "",
                        videoMeta?.hqImageUrl ?: "",
                        ytFiles.get(18)?.format?.ext?:"mp4"
                    )
                    _states.value = StoryStates.AddStory(story)
                }
//                viewModelScope.launch(Dispatchers.IO) {
//                    repository.insertStory(story)
//                }
            }
        }.extract("http://youtube.com/watch?v=${key}")
    }

    override fun onCleared() {
        _states.value = StoryStates.Idle
        super.onCleared()
    }

    fun downloadVideo(model :StoryEntity){
        val workManger = WorkManager.getInstance(getApplication())
        val data = Data.Builder().putString(VIDEO_KEY,Gson().toJson(model)).build()
        val worker = OneTimeWorkRequest.Builder(VideoDownloader::class.java)
            .setInputData(data)
            .build()
        workManger.enqueue(worker)
        _states.value = StoryStates.DownloadVideo(workManger.getWorkInfoByIdLiveData(worker.id))
    }


}