package com.mabrouk.dalilmuslim.di

import com.mabrouk.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun getRepository(repository: QuranRepository) : QuranDefultRepository

    @Binds
    @Singleton
    abstract fun getHadithRepository(repository: HadithRepository) : HadithMyDafaultRepository

    @Binds
    @Singleton
    abstract fun getStoryRepository(repository: StoryRepository) : StoryDefaultRepository

    @Binds
    @Singleton
    abstract fun getRadioRepository(repository: RadioRepository) : RadioDefaultRepository

}