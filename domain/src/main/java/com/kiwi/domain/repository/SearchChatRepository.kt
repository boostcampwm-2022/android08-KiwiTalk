package com.kiwi.domain.repository

import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import kotlinx.coroutines.flow.Flow

interface SearchChatRepository {
    fun getMarkerList(keywords: List<String>, x: Double, y: Double): Flow<Marker>

    suspend fun getChat(cid: String): ChatInfo
}