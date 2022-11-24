package com.kiwi.data.datasource.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.kiwi.data.Const
import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.domain.model.ChatInfo
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.querysort.QuerySortByField
import com.kiwi.data.mapper.Mapper.toChatInfo
import io.getstream.chat.android.client.models.Filters
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SearchChatRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val chatClient: ChatClient,
) : SearchChatRemoteDataSource {
    override suspend fun getMarkerList(
        keyword: List<String>,
        x: Double,
        y: Double
    ): Flow<MarkerRemote> = callbackFlow {
        //TODO: Chat 컬렉션의 모든 데이터를 가져오지 않고, 쿼리를 사용하도록 변경
        firestore.collection(Const.CHAT_COLLECTION).get()
            .addOnSuccessListener {
                Log.d("SearchChatRemoteImpl", "getMarkerList: ${it.documents}")
                it.toObjects<MarkerRemote>().forEach { markerRemote ->
                    trySend(markerRemote)
                }
            }.addOnFailureListener {
                Log.d("SearchChatRemoteImpl", "getMarkerList: $it")
            }
        awaitClose()
    }

    override suspend fun getChat(cid: String): ChatInfo? {
        val request = QueryChannelsRequest(
            filter = Filters.and(
                Filters.eq("cid", cid),
            ),
            offset = 0,
            limit = 10,
            querySort = QuerySortByField.descByName("member_count")
        ).apply {
            watch = true // if true returns the Channel state
            state = true // if true listen to changes to this Channel in real time.
            limit = ONE  // The number of channels to return (max is 30)
        }

        //val user = User(id = "kimgyeon2", name = "")
        //chatClient.devToken(user.id)

        Log.d(TAG, chatClient.getCurrentUser().toString())

        /* await는 실패하면 터진다. 코루틴 Result 보기싫다고 빼면 안된다. await 동작 잘 보자 */
        val result = chatClient.queryChannels(request).await()
        return if(result.isSuccess){
            Log.d(TAG, result.toString())
            result.data().first().toChatInfo()
        } else {
            Log.d(TAG, result.toString())
            null
        }
    }

    companion object{
        private const val ONE = 1
        private const val TAG = "k001"
    }
}