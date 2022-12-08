package com.kiwi.data.datasource.remote

import com.kiwi.data.UserDataCallback
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {
    fun login(token: String, callback: UserDataCallback)
    fun updateUser(user: User)
    suspend fun signOut(): Flow<Boolean>
}