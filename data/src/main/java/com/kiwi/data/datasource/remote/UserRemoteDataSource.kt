package com.kiwi.data.datasource.remote

import io.getstream.chat.android.client.models.User

interface UserRemoteDataSource {
    suspend fun login(token: String, name: String, imageUrl: String): Result<User>
}