package com.kiwi.data.datasource.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.domain.model.keyword.KeywordCategory
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchKeywordRemoteDataSource @Inject constructor(
    val firestore: FirebaseFirestore
) {
    suspend fun callAllKeyword(): List<KeywordCategory> {
        val keywordCategoryList: MutableList<KeywordCategory> = mutableListOf()

        firestore.collection("keywords").get()
                .addOnSuccessListener { result ->
                    result.documents.forEach {
                        val keywordCategory = KeywordCategory(it.id, mutableListOf())
                        it.data?.values?.forEach {
                            (it as HashMap<*, *>).let { keyword ->
                                keywordCategory.keywords.add(
                                    Keyword(
                                        keyword.get("name").toString(),
                                        (keyword.get("count") as Long).toInt()
                                    )
                                )
                            }
                        }
                        keywordCategoryList.add(keywordCategory)
                        Log.d("FIRESTORE_CALL_KEYWORD", "callAllKeyword: sucess data : ${keywordCategory}")
                    }
                    Log.d("FIRESTORE_CALL_KEYWORD", "callAllKeyword: sucess resultString : ${keywordCategoryList}")
                }
                .addOnFailureListener {
                    Log.d("FIRESTORE_CALL_KEYWORD", "callAllKeyword: fail")
                }.await()

        return keywordCategoryList.toList()
    }

}