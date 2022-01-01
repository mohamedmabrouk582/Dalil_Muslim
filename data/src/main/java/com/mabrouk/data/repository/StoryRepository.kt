package com.mabrouk.data.repository

import com.mabrouk.data.db.story.StoryDao
import com.mabrouk.data.entities.StoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 27/08/2021 created by Just clean
 */
class StoryRepository @Inject constructor(val dao: StoryDao) : StoryDefaultRepository {
    override suspend fun insertStory(storyEntity: StoryEntity): Long {
        return dao.insertStory(storyEntity)
    }

    override  fun getStories(): Flow<List<StoryEntity>> {
        return dao.getStories()
    }

    override  fun searchStory(query: String): Flow<StoryEntity> {
       return dao.search(query)
    }
}