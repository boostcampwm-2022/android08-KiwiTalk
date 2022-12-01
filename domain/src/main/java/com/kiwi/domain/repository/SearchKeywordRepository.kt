package com.kiwi.domain.repository

import com.kiwi.domain.model.KeywordCategory

interface SearchKeywordRepository {

    suspend fun getAllKeyWord(): List<KeywordCategory>

}