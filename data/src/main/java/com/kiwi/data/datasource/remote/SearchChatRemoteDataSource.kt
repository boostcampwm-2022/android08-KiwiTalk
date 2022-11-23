package com.kiwi.data.datasource.remote

import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.domain.model.ChatInfo
import kotlinx.coroutines.flow.Flow

interface SearchChatRemoteDataSource {
    suspend fun getMarkerList(keyword: List<String>, x: Double, y: Double): Flow<MarkerRemote>

    suspend fun getChat(cid: String): ChatInfo?
}