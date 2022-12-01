package com.kiwi.data.repository

import com.kiwi.data.datasource.remote.NewChatRemoteDataSource
import com.kiwi.data.mapper.Mapper.toNewChatRemote
import com.kiwi.domain.model.NewChatInfo
import com.kiwi.domain.repository.NewChatRepository
import javax.inject.Inject

class NewChatRepositoryImpl @Inject constructor(
    private val dateSource: NewChatRemoteDataSource
): NewChatRepository {
    override suspend fun addChat(
        userId: String,
        currentTime: String,
        newChat: NewChatInfo
    ) {
        dateSource.addChat(userId,currentTime,newChat.toNewChatRemote())
    }
}