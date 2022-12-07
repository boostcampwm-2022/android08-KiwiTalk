package com.kiwi.kiwitalk.di

import com.kiwi.data.datasource.local.UserLocalDataSource
import com.kiwi.data.datasource.local.UserLocalDataSourceImpl
import com.kiwi.data.datasource.remote.*
import com.kiwi.data.repository.*
import com.kiwi.domain.repository.*
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

    @Binds
    @Singleton
    abstract fun bindsSearchPlaceRepository(searchPlaceRepositoryImpl: SearchPlaceRepositoryImpl): SearchPlaceRepository

    @Binds
    @Singleton
    abstract fun bindsSearchPlaceRemoteDataSource(searchPlaceRemoteDataSourceImpl: SearchPlaceRemoteDataSourceImpl): SearchPlaceRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsNewChatRemoteDataSource(newChatRemoteDataSourceImpl: NewChatRemoteDataSourceImpl): NewChatRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsNewChatRepository(newChatRepositoryImpl: NewChatRepositoryImpl): NewChatRepository

    @Binds
    @Singleton
    abstract fun bindsProfileSettingRepository(profileSettingRepositoryImpl: ProfileSettingRepositoryImpl): ProfileSettingRepository

    @Binds
    @Singleton
    abstract fun bindExitChatDataSource(exitChatRemoteDataSourceImpl: ExitChatRemoteDataSourceImpl): ExitChatRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindExitChatRepository(exitChatRepositoryImpl: ExitChatRepositoryImpl): ExitChatRepository

    @Binds
    @Singleton
    abstract fun bindUserLocalDatasource(dataImpl: UserLocalDataSourceImpl): UserLocalDataSource

    @Binds
    @Singleton
    abstract fun bindUserRemoteDatasource(dataImpl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserRepository(repoImpl: UserRepositoryImpl): UserRepository
}