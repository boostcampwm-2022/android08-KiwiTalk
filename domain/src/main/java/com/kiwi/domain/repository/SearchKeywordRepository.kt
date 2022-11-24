package com.kiwi.domain.repository

import com.kiwi.domain.model.keyword.KeywordCategory

interface SearchKeywordRepository {

    suspend fun getAllKeyWord(): List<KeywordCategory>

}