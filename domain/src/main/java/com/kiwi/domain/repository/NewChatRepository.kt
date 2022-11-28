package com.kiwi.domain.repository

import com.kiwi.domain.model.NewChat

interface NewChatRepository {
    suspend fun addFireBaseChat(newChatInfo: NewChat)
}