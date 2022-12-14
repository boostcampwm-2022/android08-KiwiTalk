package com.kiwi.data.datasource.remote

import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import kotlinx.coroutines.flow.Flow

interface SearchChatRemoteDataSource {
    fun getMarkerList(keyword: List<String>, x: Double, y: Double): Flow<Marker>
    suspend fun getChatList(cidList: List<String>): List<ChatInfo>?
    fun appendUserToChat(cid: String, userId: String)
}