package com.kiwi.data.datasource.remote

import com.kiwi.data.model.remote.PlaceListRemote
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("v2/local/search/keyword.json")
    suspend fun getSearchKeyword(
        @Query("x") lng: String,
        @Query("y") lat: String,
        @Query("radius") radius: String,
        @Query("query") place: String
    ): Response<PlaceListRemote>
}