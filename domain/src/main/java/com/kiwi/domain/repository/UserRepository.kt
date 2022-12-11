package com.kiwi.domain.repository

import com.kiwi.domain.UserUiCallback
import com.kiwi.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun isRemoteLoginRequired(userUiCallback: UserUiCallback)
    fun tryLogin(
        token: String, googleName: String, imageUrl: String, userUiCallback: UserUiCallback
    )
    suspend fun signOut(): Flow<Boolean>
    fun getUserInfo(): UserInfo
    fun updateUser(userInfo: UserInfo)
}