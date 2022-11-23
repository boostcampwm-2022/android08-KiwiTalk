package com.kiwi.kiwitalk.di

import com.kiwi.data.datasource.remote.SearchChatRemoteDataSource
import com.kiwi.data.datasource.remote.SearchChatRemoteDataSourceImpl
import com.kiwi.data.repository.SearchChatRepositoryImpl
import com.kiwi.data.repository.SearchKeywordRepositoryImpl
import com.kiwi.domain.repository.SearchChatRepository
import com.kiwi.domain.repository.SearchKeywordRepository
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

    @Singleton
    @Binds
    abstract fun bindsSearchChatRemoteDataSource(dataSource: SearchChatRemoteDataSourceImpl): SearchChatRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsSearchKeywordRepository(searchKeywordRepositoryImpl: SearchKeywordRepositoryImpl): SearchKeywordRepository
}