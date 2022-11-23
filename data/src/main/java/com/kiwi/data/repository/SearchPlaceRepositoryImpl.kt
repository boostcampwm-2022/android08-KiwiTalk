package com.kiwi.data.repository

import com.kiwi.data.datasource.remote.SearchPlaceRemoteDataSource
import com.kiwi.domain.ApiResult
import com.kiwi.domain.ResultSearchPlace
import com.kiwi.domain.repository.SearchPlaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPlaceRepositoryImpl @Inject constructor(
    private val dataSource: SearchPlaceRemoteDataSource
): SearchPlaceRepository {

    override suspend fun getSearchKeyword(
        lng: String,
        lat: String,
        place: String
    ): ApiResult<ResultSearchPlace> {
        return dataSource.getSearchKeyword(lng,lat,place)
    }

    //    override suspend fun getSearchKeyword(
//        lng:String,
//        lat:String,
//        place: String
//    ): Flow<ResultSearchPlace> = dataSource.getSearchKeyword(lng,lat,place)

}