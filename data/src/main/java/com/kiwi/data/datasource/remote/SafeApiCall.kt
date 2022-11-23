package com.kiwi.data.datasource.remote

import com.kiwi.domain.ApiResult
import retrofit2.Response
import java.io.IOException

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>?): ApiResult<T> {
    val response: Response<T>?

    try {
        response = call()
    } catch (e: IOException) {
        return ApiResult.Error(IOException(e.message ?: "Internet error runs"))
    }

    val body = response?.body()
    return if (body != null && response.isSuccessful) {
        ApiResult.Success(body)
    } else {
        ApiResult.Error(IOException(response?.message()))
    }
}


//@ExperimentalCoroutinesApi
//fun <T> Call<T>.asCallbackFLow() = callbackFlow<T> {
//    enqueue(object : Callback<T> {
//        // 응답을 받은경우의 호출
//        override fun onResponse(call: Call<T>, response: Response<T>) {
//
//            if (response.isSuccessful) {
//                response.body()?.let { trySend(it).isSuccess } ?: close()
//            } else {
//                close()
//            }
//        }
//
//        //호출이 실패한 경우
//        override fun onFailure(call: Call<T>, throwable: Throwable) {
//            close(throwable)
//        }
//    })
//
//    awaitClose() //close가 호출될때까지 기다립니다.
//}