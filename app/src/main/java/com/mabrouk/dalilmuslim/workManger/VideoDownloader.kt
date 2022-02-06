package com.mabrouk.dalilmuslim.workManger

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.mabrouk.dalilmuslim.utils.AudioDataPass
import com.mabrouk.dalilmuslim.utils.VIDEO_KEY
import com.mabrouk.data.entities.StoryEntity
import com.mabrouk.data.repository.AyaRepository
import com.mabrouk.data.utils.FileUtils
import com.mabrouk.domain.utils.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 06/02/2022 created by Just clean
 */
@HiltWorker
class VideoDownloader @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    val repository: AyaRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
           var result : Result = Result.failure()
        val storyEntity = Gson().fromJson(inputData.getString(VIDEO_KEY)?:"",StoryEntity::class.java)
           repository.downloadAudio(storyEntity.url).collectLatest {
               when(it){
                   is com.mabrouk.domain.utils.Result.NoInternetConnect -> {
                       result = Result.failure()
                   }
                   is com.mabrouk.domain.utils.Result.OnFailure -> {
                       result = Result.failure()
                   }
                   is com.mabrouk.domain.utils.Result.OnLoading -> {

                   }
                   is com.mabrouk.domain.utils.Result.OnSuccess -> {
                       FileUtils.saveVideo(it.data,storyEntity)
                       result = Result.success()
                   }
               }
           }
       return result
    }




}