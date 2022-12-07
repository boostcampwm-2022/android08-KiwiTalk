package com.kiwi.data.datasource.local

interface UserLocalDataSource {
    suspend fun saveToken(id: String?, name: String?, imageUrl: String?)
    fun getToken(): String
    suspend fun deleteToken()
}