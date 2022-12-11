package com.kiwi.domain.repository

interface ExitChatRepository {
    suspend fun exitChat(cid: String): Result<String>
}
