package com.kiwi.data.datasource.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchKeywordRemoteDataSource @Inject constructor(
    val firestore: FirebaseFirestore
) {

    suspend fun callAllKeyword(): String? {
        var resultString: String = ""


        firestore.collection("keywords").get()
                .addOnSuccessListener { result ->
                    result.documents.forEach {
                        resultString += it.id
                        Log.d("FIRESTORE_CALL_KEYWORD", "callAllKeyword: sucess data : ${it.id}")
                    }
                    Log.d("FIRESTORE_CALL_KEYWORD", "callAllKeyword: sucess resultString : ${resultString}")
                }
                .addOnFailureListener {
                    Log.d("FIRESTORE_CALL_KEYWORD", "callAllKeyword: fail")
                }.await()


        return resultString.also { Log.d("FIRESTORE_CALL_KEYWORD", "return: ") }
    }

}