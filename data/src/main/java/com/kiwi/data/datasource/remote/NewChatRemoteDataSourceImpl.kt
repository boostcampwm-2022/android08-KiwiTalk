package com.kiwi.data.datasource.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kiwi.data.model.remote.NewChatRemote
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import javax.inject.Inject

class NewChatRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val chatClient: ChatClient,
): NewChatRemoteDataSource {

    override suspend fun addChat(
        userId: String,
        currentTime: String,
        newChatRemote: NewChatRemote
    ) {
        val fireStoreData = hashMapOf(
            "cid" to userId + currentTime ,
            "keywords" to newChatRemote.keywords,
            "lat" to newChatRemote.lat,
            "lng" to newChatRemote.lng
        )

        firestore.collection("chat_list").document().set(fireStoreData)
            .addOnSuccessListener {
                Log.d("addFireBaseChat", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                Log.d("addFireBaseChat", "Error writing document")
            }


        val streamData = hashMapOf(
            "image" to newChatRemote.imageUri,
            "name" to newChatRemote.chatName,
            "description" to newChatRemote.chatDescription,
            "address" to newChatRemote.address,
            "keyword" to newChatRemote.keywords,
            "max_member_cnt" to newChatRemote.maxMemberCnt
        )

        val token = chatClient.devToken(userId) // developer 토큰 생성
        chatClient.connectUser( // 유저 로그인
            user = User(id = userId),
            token = token
        ).enqueue {
            // Step 4 - 새로운 그룹 (채널) 생성
            if (it.isSuccess) {
                chatClient.createChannel(
                    channelType = "messaging",
                    channelId = userId + currentTime,
                    memberIds = listOf(userId),
                    extraData = streamData
                ).enqueue()
            }
        }
    }
}