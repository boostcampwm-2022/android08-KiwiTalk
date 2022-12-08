package com.kiwi.data.datasource.remote

import com.kiwi.data.UserDataCallback
import io.getstream.chat.android.client.models.User

interface UserRemoteDataSource {
    fun login(token: String, callback: UserDataCallback)
    fun updateUser(user: User)
}