package com.kiwi.data.repository

import android.util.Log
import com.kiwi.data.datasource.remote.SearchChatRemoteDataSource
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import com.kiwi.domain.repository.SearchChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchChatRepositoryImpl @Inject constructor(
    private val dataSource: SearchChatRemoteDataSource
) : SearchChatRepository {
    override fun getMarkerList(
        keywords: List<String>,
        x: Double,
        y: Double
    ): Flow<Marker> = flow {
        Log.d("SearchChatRepository", "getMarkerList: start flow")
        dataSource.getMarkerList(keywords, x, y).collect() { marker ->
            Log.d("SearchChatRepository", "getMarkerList: $marker")
            emit(marker)
        }
    }

    override suspend fun getChat(cid: String): ChatInfo {
        TODO("Not yet implemented")
    }
}