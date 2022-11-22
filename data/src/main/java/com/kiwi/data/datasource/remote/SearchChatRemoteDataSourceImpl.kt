package com.kiwi.data.datasource.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.kiwi.data.Const
import com.kiwi.data.model.remote.ChatInfoRemote
import com.kiwi.data.model.remote.MarkerRemote
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SearchChatRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
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

    override suspend fun getChat(cid: String): ChatInfoRemote {
        TODO("Not yet implemented")
    }
}