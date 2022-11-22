package com.kiwi.domain.repository

interface SearchKeywordRepository {

    suspend fun getAllKeyWord(): String?

}