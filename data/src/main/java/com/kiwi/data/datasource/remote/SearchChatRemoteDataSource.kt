package com.kiwi.data.datasource.remote

import com.kiwi.data.model.remote.ChatInfoRemote
import com.kiwi.domain.model.Marker
import kotlinx.coroutines.flow.Flow

interface SearchChatRemoteDataSource {
    fun getMarkerList(keyword: List<String>, x: Double, y: Double): Flow<Marker>

    suspend fun getChat(cid: String): ChatInfoRemote
}