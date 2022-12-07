package com.kiwi.data

import io.getstream.chat.android.client.models.User

interface UserDataCallback {
    fun onSuccess(user: User)
    fun onFailure(e: Throwable)
}