package com.kiwi.data.datasource.remote

import com.kiwi.data.model.remote.NewChatRemote

interface NewChatRemoteDataSource {
    suspend fun addChatUpload(userId: String, currentTime: String, newChatRemote: NewChatRemote)
}