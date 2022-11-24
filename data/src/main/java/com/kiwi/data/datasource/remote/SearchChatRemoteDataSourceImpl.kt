package com.kiwi.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.kiwi.data.Const
import com.kiwi.data.mapper.Mapper.toMarker
import com.kiwi.data.model.remote.ChatInfoRemote
import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.domain.model.Marker
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SearchChatRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
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

    override suspend fun getChat(cid: String): ChatInfoRemote {
        TODO("Not yet implemented")
    }

    companion object {
        private val Double.toRange get() = this - 1..this + 1
    }
}