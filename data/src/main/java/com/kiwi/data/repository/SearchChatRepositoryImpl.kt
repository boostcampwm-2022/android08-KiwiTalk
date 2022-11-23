package com.kiwi.data.repository

import android.util.Log
import com.kiwi.data.datasource.remote.SearchChatRemoteDataSource
import com.kiwi.data.mapper.Mapper.toMarker
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.PlaceChatInfo
import com.kiwi.domain.repository.SearchChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchChatRepositoryImpl @Inject constructor(
    private val dataSource: SearchChatRemoteDataSource,
) : SearchChatRepository {
    override suspend fun getMarkerList(
        keywords: List<String>,
        x: Double,
        y: Double
    ): Flow<Marker> = flow {
        Log.d("SearchChatRepository", "getMarkerList: start flow")
        dataSource.getMarkerList(keywords, x, y).collect() { markerRemote ->
            Log.d("SearchChatRepository", "getMarkerList: $markerRemote")
            emit(markerRemote.toMarker())
        }
    }

    override suspend fun getChat(marker: Marker): PlaceChatInfo {
        val chatList = dataSource.getChat(marker.cid)
            ?.let { listOf(it) } ?: listOf<ChatInfo>()

        return PlaceChatInfo(chatList)
    }
}