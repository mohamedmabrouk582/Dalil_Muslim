package com.mabrouk.dalilmuslim.workManger

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.mabrouk.dalilmuslim.utils.*
import com.mabrouk.data.entities.StoryEntity
import com.mabrouk.data.repository.QuranRepository
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class MediaPlayerWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {
    var items: ArrayList<StoryEntity> = ArrayList()
    override suspend fun doWork(): Result {
        inputData.getString(STORY_ENTITY_LIST)?.apply {
            val type = object : TypeToken<ArrayList<StoryEntity>>() {}.type
            items = Gson().fromJson(this, type)
        }
        return Result.success()
    }


}