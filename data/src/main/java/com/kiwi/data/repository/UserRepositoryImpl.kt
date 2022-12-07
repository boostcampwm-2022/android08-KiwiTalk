package com.kiwi.data.repository

import android.util.Log
import com.kiwi.data.Const
import com.kiwi.data.datasource.local.UserLocalDataSource
import com.kiwi.data.datasource.remote.UserRemoteDataSource
import com.kiwi.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository {

    override suspend fun isRemoteLoginRequired(): Result<Boolean> {
        val savedToken = userLocalDataSource.getToken()
        return login(savedToken, Const.EMPTY_STRING, Const.EMPTY_STRING)
    }

    override suspend fun tryLogin(token: String, name: String, imageUrl: String): Result<Boolean> {
        return login(token, name, imageUrl)
    }

    private suspend fun login(token: String, name: String, imageUrl: String): Result<Boolean> {
        return kotlin.runCatching {
            if (!isValidToken(token)) {
                userLocalDataSource.deleteToken()
                throw INVALID_TOKEN
            }

            val loginResult = userRemoteDataSource.login(token, name, imageUrl)
            val userInfo = loginResult.getOrNull()

            if(userInfo == null) {
                throw loginResult.exceptionOrNull()!!
            } else {
                userInfo.let {
                    userLocalDataSource.saveToken(it.id, it.name, it.image)
                }
                true
            }
        }
    }

    private fun isValidToken(token: String): Boolean {
        return tokenRegex.matches(token)
    }

    companion object {
        private const val TAG = "k001|UserRepo"
        private val tokenRegex = Regex("[0-9,a-z]{1,21}")
        private val INVALID_TOKEN = Exception("Invalid Token")
    }
}