package com.kiwi.data.datasource.remote

import com.kiwi.domain.ApiResult
import com.kiwi.domain.ResultSearchPlace
import kotlinx.coroutines.flow.Flow

interface SearchPlaceRemoteDataSource {
    suspend fun getSearchKeyword(lng:String,lat:String,place: String): ApiResult<ResultSearchPlace>
}