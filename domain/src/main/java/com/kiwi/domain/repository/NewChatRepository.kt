package com.kiwi.domain.repository

import com.kiwi.domain.model.NewChat

interface NewChatRepository {

    suspend fun addFireBaseChat(cid:String, newChat: NewChat)

    suspend fun addStreamChat(userId: String, currentTime: String, newChat: NewChat)
}