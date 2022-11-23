package com.kiwi.data.datasource.remote

import com.kiwi.domain.ApiResult
import com.kiwi.domain.ResultSearchPlace
import retrofit2.Retrofit
import javax.inject.Inject

class SearchPlaceRemoteDataSourceImpl @Inject constructor(
    client: Retrofit?
) : SearchPlaceRemoteDataSource {

    private val searchService = client?.create(SearchService::class.java)
    override suspend fun getSearchKeyword(
        lng: String,
        lat: String,
        place: String
    ): ApiResult<ResultSearchPlace> {
       return safeApiCall { searchService?.getSearchKeyword(lng,lat,"15000",place) }
    }
}