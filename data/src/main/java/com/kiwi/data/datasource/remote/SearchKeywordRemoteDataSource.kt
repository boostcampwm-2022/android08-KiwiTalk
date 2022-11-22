package com.kiwi.data.datasource.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class SearchKeywordRemoteDataSource @Inject constructor(
    val firestore: FirebaseFirestore
) {

    suspend fun callAllKeyword(): String?{
        var resultString: String? = null
        val firestoreAsync = CoroutineScope(Dispatchers.Default).async {
            firestore.collection("keywords").get()
                .addOnSuccessListener { result ->
                    result.documents.forEach {
                        resultString += it.data?.toString()
                    }
                }
                .addOnFailureListener {
                    Log.d("FIRESTORE_CALL_KEYWORD", "callAllKeyword: ")
                }
        }
        firestoreAsync.await()
        return resultString
    }

}