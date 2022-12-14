package com.kiwi.data.repository

import android.util.Log
import com.kiwi.data.datasource.local.UserLocalDataSource
import com.kiwi.data.datasource.remote.SearchChatRemoteDataSource
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Keyword
import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.PlaceChatInfo
import com.kiwi.domain.repository.SearchChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchChatRepositoryImpl @Inject constructor(
    private val searchChatDataSource: SearchChatRemoteDataSource,
    private val userDataSource: UserLocalDataSource,
) : SearchChatRepository {
    override fun getMarkerList(
        keywords: List<Keyword>,
        x: Double,
        y: Double
    ): Flow<Marker> {
        Log.d("SearchChatRepository", "getMarkerList: start flow")
        return searchChatDataSource.getMarkerList(keywords.map { it.name }, x, y)
    }

    override suspend fun getPlaceChatList(cidList: List<String>): PlaceChatInfo {
        val chatList = searchChatDataSource.getChatList(cidList) ?: mutableListOf<ChatInfo>()
        return PlaceChatInfo(chatList)
    }

    override fun appendUserToChat(cid: String){
        val userId = userDataSource.getToken()
        if (userId.isNotEmpty()){
            searchChatDataSource.appendUserToChat(cid, userId)
        }
    }
}