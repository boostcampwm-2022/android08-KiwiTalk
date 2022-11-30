package com.kiwi.domain.repository

import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.PlaceChatInfo
import com.kiwi.domain.model.keyword.Keyword
import kotlinx.coroutines.flow.Flow

interface SearchChatRepository {
    fun getMarkerList(keywords: List<Keyword>, x: Double, y: Double): Flow<Marker>

    suspend fun getChat(marker: Marker): PlaceChatInfo
}