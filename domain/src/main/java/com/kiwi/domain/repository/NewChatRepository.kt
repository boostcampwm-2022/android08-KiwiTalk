package com.kiwi.domain.repository

import com.kiwi.domain.model.NewChatInfo

interface NewChatRepository {
    suspend fun addChat(userId: String, currentTime: String, newChat: NewChatInfo)
}