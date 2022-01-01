package com.mabrouk.data.db.story

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mabrouk.data.entities.StoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 27/08/2021 created by Just clean
 */
@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(storyEntity: StoryEntity) : Long

    @Query("select * from StoryEntity")
    fun getStories() : Flow<List<StoryEntity>>

    @Query("select * from StoryEntity where title LIKE '%' || :query || '%'")
    fun search(query:String) : Flow<StoryEntity>
}