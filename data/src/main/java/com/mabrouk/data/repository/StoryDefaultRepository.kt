package com.mabrouk.data.repository

import com.mabrouk.data.entities.StoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 27/08/2021 created by Just clean
 */
interface StoryDefaultRepository {
    suspend fun insertStory(storyEntity: StoryEntity) : Long
     fun getStories() : Flow<List<StoryEntity>>
     fun searchStory(query:String) : Flow<StoryEntity>
}