package com.kiwi.data.datasource.remote

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.utils.onError
import io.getstream.chat.android.client.utils.onSuccess
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ExitChatRemoteDataSourceImpl @Inject constructor(private val chatClient: ChatClient) :
    ExitChatRemoteDataSource {
    override suspend fun exitChat(cid: String): Result<Channel> {
//        chatClient.channel(cid).removeMembers(listOf(userId)).execute()
        val userIdList = listOfNotNull(chatClient.getCurrentUser()?.id)
        return suspendCoroutine { continuation ->
            chatClient.channel(cid).removeMembers(userIdList).enqueue() { it ->
                it.onSuccess {
                    continuation.resume(Result.success(it))
                }.onError {
                    continuation.resumeWithException(it.cause ?: Throwable())
                }
            }
        }
    }
}