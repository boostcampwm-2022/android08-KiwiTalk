package com.kiwi.data.repository

import com.kiwi.data.datasource.remote.ExitChatRemoteDataSource
import com.kiwi.domain.repository.ExitChatRepository
import javax.inject.Inject

class ExitChatRepositoryImpl @Inject constructor(
    private val datasource: ExitChatRemoteDataSource
) : ExitChatRepository {
    override suspend fun exitChat(cid: String): Result<String> {
        return datasource.exitChat(cid).map { it.cid }
    }
}