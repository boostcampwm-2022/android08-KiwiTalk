package com.kiwi.domain.repository

interface UserRepository {
    suspend fun isRemoteLoginRequired(): Result<Boolean>
    suspend fun tryLogin(token: String, name: String, imageUrl: String): Result<Boolean>

}