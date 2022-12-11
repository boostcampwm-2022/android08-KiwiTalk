package com.kiwi.data.repository

import com.kiwi.data.UserDataCallback
import com.kiwi.data.datasource.local.UserLocalDataSource
import com.kiwi.data.datasource.remote.UserRemoteDataSource
import com.kiwi.data.mapper.Mapper.toUserInfo
import com.kiwi.domain.UserUiCallback
import com.kiwi.domain.model.UserInfo
import com.kiwi.domain.repository.UserRepository
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository {

    override fun isRemoteLoginRequired(userUiCallback: UserUiCallback) {
        val token = userLocalDataSource.getToken()
        if (!isValidToken(token)) {
            userLocalDataSource.deleteToken()
            userUiCallback.onFailure(INVALID_TOKEN)
            return
        }

        userRemoteDataSource.login(token, object : UserDataCallback {
            override fun onSuccess(user: User) {
                if (user.id.isNotEmpty()) {
                    userUiCallback.onSuccess()
                } else {
                    userUiCallback.onFailure(NO_DATA)
                }
            }

            override fun onFailure(e: Throwable) {
                userUiCallback.onFailure(e)
            }
        })
    }

    override fun tryLogin(
        token: String,
        googleName: String,
        imageUrl: String,
        userUiCallback: UserUiCallback
    ) {
        if (!isValidToken(token)) {
            throw INVALID_TOKEN
        }
        userRemoteDataSource.login(token, object : UserDataCallback {
            override fun onSuccess(user: User) {
                with(user) {
                    if (id.isEmpty()) {
                        userUiCallback.onFailure(NO_DATA)
                    } else if (name.isEmpty()) {
                        userRemoteDataSource.updateUser(
                            User(id = this.id, name = googleName, image = imageUrl)
                        )
                    } else {
                        userLocalDataSource.saveToken(id, name, image)
                    }
                }
                userUiCallback.onSuccess()
            }

            override fun onFailure(e: Throwable) {
                userUiCallback.onFailure(e)
            }
        })
    }

    override suspend fun signOut(): Flow<Boolean> = flow {
        userRemoteDataSource.signOut().collect {
            if (it) userLocalDataSource.deleteToken()
            emit(it)
        }
    }


    private fun isValidToken(token: String): Boolean {
        return tokenRegex.matches(token)
    }

    override fun getUserInfo(): UserInfo {
        userRemoteDataSource.getCurrentUser()?.let {
            lastUser = it.toUserInfo()
        }
        return lastUser
    }

    companion object {
        private const val TAG = "k001|UserRepo"
        private val tokenRegex = Regex("[0-9,a-z]{1,21}")
        private val INVALID_TOKEN = Exception("Invalid Token")
        private val NO_DATA = Exception("Stream에서 Id값이 Empty String으로 반환됨")
        private var lastUser: UserInfo = UserInfo("","", listOf())
    }
}