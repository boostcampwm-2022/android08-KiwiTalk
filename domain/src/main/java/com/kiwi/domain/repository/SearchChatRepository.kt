package com.kiwi.domain.repository

import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.PlaceChatInfo
import com.kiwi.domain.model.Keyword
import kotlinx.coroutines.flow.Flow

interface SearchChatRepository {
    fun getMarkerList(keywords: List<Keyword>, x: Double, y: Double): Flow<Marker>
    suspend fun getPlaceChatList(cidList: List<String>): PlaceChatInfo
    fun appendUserToChat(cid: String)
}