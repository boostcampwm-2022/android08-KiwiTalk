package com.kiwi.data.datasource.remote

import io.getstream.chat.android.client.models.Channel

interface ExitChatRemoteDataSource {
    suspend fun exitChat(cid: String): Result<Channel>
}