package com.kiwi.kiwitalk.di

import com.kiwi.data.repository.SearchChatRepositoryImpl
import com.kiwi.domain.repository.SearchChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds //Interface 주입, abstract 사용
    abstract fun bindsSearchChatRepository(repository: SearchChatRepositoryImpl): SearchChatRepository
}