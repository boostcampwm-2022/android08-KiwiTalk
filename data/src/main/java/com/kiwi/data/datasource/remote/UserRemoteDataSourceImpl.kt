package com.kiwi.data.datasource.remote

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val chatClient: ChatClient
) : UserRemoteDataSource {

    override suspend fun login(token: String, name: String, imageUrl: String): Result<User> {
        // repository에서 토큰 검사는 끝냈음
        val devToken = chatClient.devToken(token)
        val existUser = chatClient.getCurrentUser()

        return kotlin.runCatching {
            if (existUser != null) {
                existUser
            } else {
                val newUser = User(
                    id = token,
                    name = "김경연",
                    image = "https://lh3.googleusercontent.com/a/AEdFTp787XnwxaxCT6NjvFaYe5a7wQ3lNR0maYT1M8Kl=s96-c"
                )
                val result = chatClient.connectUser(newUser, devToken).execute()
                if (result.isSuccess) {
                    result.data().user
                } else {
                    throw result.error().cause!!
                }
            }
        }
    }
}