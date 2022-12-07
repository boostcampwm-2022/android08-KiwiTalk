package com.kiwi.data.datasource.local

interface UserLocalDataSource {
    suspend fun saveToken(id: String?, name: String?, imageUrl: String?)
    suspend fun getToken(): String
    fun isValidToken(token: String): Boolean
}