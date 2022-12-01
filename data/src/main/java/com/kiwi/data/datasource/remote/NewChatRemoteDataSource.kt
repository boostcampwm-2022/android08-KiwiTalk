package com.kiwi.data.datasource.remote

import com.google.type.LatLng
import com.kiwi.data.model.remote.NewChatRemote
import kotlinx.coroutines.flow.Flow

interface NewChatRemoteDataSource {
    suspend fun addChatUpload(userId: String, currentTime: String, newChatRemote: NewChatRemote)
}