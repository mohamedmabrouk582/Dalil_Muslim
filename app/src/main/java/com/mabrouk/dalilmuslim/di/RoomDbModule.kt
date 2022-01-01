package com.mabrouk.dalilmuslim.di

import android.content.Context
import androidx.room.Room
import com.mabrouk.data.db.azkar.AzkarDao
import com.mabrouk.data.db.azkar.AzkarDb
import com.mabrouk.data.db.hadith.HadithDao
import com.mabrouk.data.db.hadith.HadithDb
import com.mabrouk.data.db.quran.QuranDB
import com.mabrouk.data.db.quran.QuranDao
import com.mabrouk.data.db.story.StoryDao
import com.mabrouk.data.db.story.StoryDb
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDbModule {

    @Provides
    @Singleton
    fun getQuranDao(@ApplicationContext context: Context): QuranDao =
        Room.databaseBuilder(context, QuranDB::class.java, "quranDb")
            .fallbackToDestructiveMigration().build().getQuranDao()


    @Provides
    @Singleton
    fun getAzkarDao(@ApplicationContext context: Context): AzkarDao =
        Room.databaseBuilder(context, AzkarDb::class.java, "azkar-db")
            .createFromAsset("azkar-db")
            .build().getAzkarDao()

    @Provides
    @Singleton
    fun getHadithDao(@ApplicationContext context: Context): HadithDao =
        Room.databaseBuilder(context, HadithDb::class.java, "hadithDb")
            .fallbackToDestructiveMigration()
            .build().getHadithDao()

    @Provides
    @Singleton
    fun getStoryDao(@ApplicationContext context: Context): StoryDao =
        Room.databaseBuilder(context, StoryDb::class.java, "story")
            .fallbackToDestructiveMigration()
            .build().getDao()

    @Provides
    @Singleton
    fun getRemoteConfig(): FirebaseRemoteConfig =
        FirebaseRemoteConfig.getInstance().apply {
            this.setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build()
            )
        }
}