package com.kiwi.domain.repository

import com.kiwi.domain.ApiResult
import com.kiwi.domain.ResultSearchPlace
import kotlinx.coroutines.flow.Flow

interface SearchPlaceRepository {
    suspend fun getSearchKeyword(lng:String,lat:String,place: String): ApiResult<ResultSearchPlace>
}