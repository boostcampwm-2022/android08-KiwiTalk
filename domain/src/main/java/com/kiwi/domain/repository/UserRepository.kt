package com.kiwi.domain.repository

import com.kiwi.domain.UserUiCallback

interface UserRepository {
    fun isRemoteLoginRequired(userUiCallback: UserUiCallback)
    fun tryLogin(token: String, googleName: String, imageUrl: String, userUiCallback: UserUiCallback)
}