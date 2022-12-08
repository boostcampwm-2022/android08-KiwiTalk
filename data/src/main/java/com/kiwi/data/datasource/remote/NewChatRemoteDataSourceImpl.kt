package com.kiwi.data.datasource.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kiwi.data.model.remote.NewChatRemote
import io.getstream.chat.android.client.ChatClient
import javax.inject.Inject

class NewChatRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val chatClient: ChatClient,
    private val storage: FirebaseStorage,
) : NewChatRemoteDataSource {

    override suspend fun addChatUpload(
        userId: String,
        currentTime: String,
        newChatRemote: NewChatRemote
    ) {
        addStreamChat(userId, currentTime, newChatRemote)
        addFireBaseChat(userId, currentTime, newChatRemote)
    }

    private fun addStreamChat(userId: String, currentTime: String, newChatRemote: NewChatRemote) {
        val cid = userId + currentTime
        if (newChatRemote.imageUri.isEmpty()) {
            addStreamChat(userId, cid, "", newChatRemote)
        } else {
            val ref = storage.reference.child("chat/$cid")
            ref.putFile(Uri.parse(newChatRemote.imageUri)).addOnSuccessListener {
                it.storage.downloadUrl.addOnCompleteListener { url ->
                    addStreamChat(userId, cid, url.result.toString(), newChatRemote)
                }
            }.addOnFailureListener {
                Log.d("NewChatDataSource", "putFile Failure: $it")
            }
        }
    }

    private fun addStreamChat(
        userId: String,
        cid: String,
        imageUrl: String,
        newChatRemote: NewChatRemote,
    ) {
        val streamData = hashMapOf(
            "image" to imageUrl,
            "name" to newChatRemote.chatName,
            "description" to newChatRemote.chatDescription,
            "address" to newChatRemote.address,
            "keywords" to newChatRemote.keywords,
            "max_member_count" to newChatRemote.maxMemberCount
        )
        chatClient.createChannel(
            channelType = "messaging",
            channelId = cid,
            memberIds = listOf(userId),
            extraData = streamData
        ).enqueue() {
            Log.d("NewChatDataSource", "createChannel: $it")
        }
    }

    private fun addFireBaseChat(userId: String, currentTime: String, newChatRemote: NewChatRemote) {
        val fireStoreData = hashMapOf(
            "cid" to "messaging:$userId$currentTime",
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
    }
}
