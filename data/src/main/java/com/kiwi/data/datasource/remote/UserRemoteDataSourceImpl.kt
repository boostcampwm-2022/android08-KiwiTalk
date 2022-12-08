package com.kiwi.data.datasource.remote

import com.kiwi.data.UserDataCallback
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val chatClient: ChatClient
) : UserRemoteDataSource {

    override fun login(token: String, callback: UserDataCallback) {
        // repository에서 토큰 검사는 끝냈음
        val devToken = chatClient.devToken(token)
        val existUser = chatClient.getCurrentUser()

        if (existUser != null) {
            callback.onSuccess(existUser)
            return
        }
        val newUser = User(id = token)
        chatClient.connectUser(newUser, devToken).enqueue { result ->
            if (result.isSuccess) {
                callback.onSuccess(result.data().user)
            } else {
                callback.onFailure(result.error().cause!!)
            }
        }
    }

    override fun updateUser(user: User) {
        chatClient.updateUser(user).enqueue()
    }

    override suspend fun signOut(): Flow<Boolean> = callbackFlow {
        chatClient.disconnect(flushPersistence = false).enqueue { disconnectResult ->
            if (disconnectResult.isSuccess) {
                trySend(true)
            } else {
                disconnectResult.error().cause?.let { throw it }
            }
        }
        awaitClose()
    }
}