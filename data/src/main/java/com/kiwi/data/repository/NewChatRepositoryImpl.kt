package com.kiwi.data.repository

import com.kiwi.data.datasource.remote.NewChatRemoteDataSource
import com.kiwi.data.mapper.Mapper.toNewChatRemote
import com.kiwi.domain.model.NewChat
import com.kiwi.domain.repository.NewChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class NewChatRepositoryImpl @Inject constructor(
    private val dateSource: NewChatRemoteDataSource
): NewChatRepository {
    override suspend fun addFireBaseChat(newChatInfo: NewChat) {
        dateSource.addFireBaseChat(newChatInfo.toNewChatRemote())
    }
}