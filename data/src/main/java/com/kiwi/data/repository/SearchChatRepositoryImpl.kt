package com.kiwi.data.repository

import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import com.kiwi.domain.repository.SearchChatRepository
import kotlinx.coroutines.flow.Flow

class SearchChatRepositoryImpl: SearchChatRepository {
    override fun getMarkerList(keywords: List<String>, x: Double, y: Double): Flow<List<Marker>> {
        TODO("Not yet implemented")
    }

    override fun getChat(cid: String): ChatInfo {
        TODO("Not yet implemented")
    }
}