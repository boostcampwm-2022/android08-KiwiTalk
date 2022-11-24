package com.kiwi.data.datasource.remote

import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.ChatInfo
import kotlinx.coroutines.flow.Flow

interface SearchChatRemoteDataSource {
    fun getMarkerList(keyword: List<String>, x: Double, y: Double): Flow<Marker>

    suspend fun getChat(cid: String): ChatInfo?
}