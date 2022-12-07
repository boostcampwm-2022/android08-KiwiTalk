package com.kiwi.data.datasource.local

interface UserLocalDataSource {
    fun saveToken(id: String?, name: String?, imageUrl: String?)
    fun getToken(): String
    fun deleteToken()
}