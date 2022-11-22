package com.kiwi.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kiwi.data.datasource.remote.SearchKeywordRemoteDataSource
import com.kiwi.domain.repository.SearchKeywordRepository
import javax.inject.Inject

class SearchKeywordRepositoryImpl @Inject constructor(
    val searchKeywordRemoteDataSource: SearchKeywordRemoteDataSource
) : SearchKeywordRepository {
    override suspend fun getAllKeyWord(): String? {
        return searchKeywordRemoteDataSource.callAllKeyword()
    }
}