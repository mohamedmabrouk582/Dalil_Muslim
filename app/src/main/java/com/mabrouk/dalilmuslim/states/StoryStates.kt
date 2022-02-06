package com.mabrouk.dalilmuslim.states

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import com.mabrouk.data.entities.JuzEntity
import com.mabrouk.data.entities.StoryEntity
import com.mabrouk.data.entities.SuraEntity

sealed class StoryStates{
    object Idle : StoryStates()
    object ShowNotification : StoryStates()
    data class AddStory(val storyEntity: StoryEntity) : StoryStates()
    data class Error(val error:String) : StoryStates()
    data class DownloadVideo(val workInfo: LiveData<WorkInfo>) : StoryStates()

}
