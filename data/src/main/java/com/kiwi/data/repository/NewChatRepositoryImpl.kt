package com.kiwi.data.repository

import com.kiwi.data.datasource.remote.NewChatRemoteDataSource
import com.kiwi.data.mapper.Mapper.toNewChatRemote
import com.kiwi.data.model.remote.NewChatRemote
import com.kiwi.domain.model.NewChat
import com.kiwi.domain.repository.NewChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class NewChatRepositoryImpl @Inject constructor(
    private val dateSource: NewChatRemoteDataSource
): NewChatRepository {
    override suspend fun addFireBaseChat(cid:String, newChat: NewChat) {
        dateSource.addFireBaseChat(cid,newChat.toNewChatRemote())
    }

    override suspend fun addStreamChat(
        userId: String,
        currentTime: String,
        newChat: NewChat
    ) {
        dateSource.addStreamChat(userId,currentTime,newChat.toNewChatRemote())
    }
}