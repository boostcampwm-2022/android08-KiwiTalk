package com.kiwi.data.datasource.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.kiwi.data.Const
import com.kiwi.data.mapper.Mapper.toChatInfo
import com.kiwi.data.mapper.Mapper.toMarker
import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField
import io.getstream.chat.android.client.models.Filters
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SearchChatRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val chatClient: ChatClient,
) : SearchChatRemoteDataSource {
    override fun getMarkerList(
        keyword: List<String>,
        x: Double,
        y: Double
    ): Flow<Marker> = callbackFlow {
        firestore.collection(Const.CHAT_COLLECTION)
            .whereArrayContainsAny("keywords", keyword).get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.toObjects<MarkerRemote>().filter { markerRemote ->
                    markerRemote.x in x.toRange && markerRemote.y in y.toRange
                }.forEach { markerRemote ->
                    trySend(markerRemote.toMarker())
                }
            }.addOnFailureListener {
                cancel()
            }
        awaitClose()
    }

    override suspend fun getChatList(cidList: List<String>): List<ChatInfo>? {
        val request = QueryChannelsRequest(
            filter = Filters.and(
                Filters.`in`("cid", cidList)
            ),
            offset = 0,
            limit = cidList.size,
            querySort = QuerySortByField.descByName("memberCount")
        ).apply {
            watch = true // if true returns the Channel state
            state = false // if true listen to changes to this Channel in real time.
            limit = cidList.size
        }

        val result = chatClient.queryChannels(request).await()
        return if (result.isSuccess) {
            Log.d(TAG, result.toString())
            result.data().map { it.toChatInfo() }
        } else {
            Log.d(TAG, result.toString())
            null
        }
    }

    companion object {
        private const val ONE = 1
        private const val TAG = "k001"
        private val Double.toRange get() = this - 1..this + 1
    }
}