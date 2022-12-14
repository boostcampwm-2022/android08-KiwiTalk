package com.kiwi.data.repository

import com.kiwi.data.datasource.remote.SearchPlaceRemoteDataSource
import com.kiwi.data.mapper.Mapper.toPlaceList
import com.kiwi.domain.model.PlaceInfoList
import com.kiwi.domain.repository.SearchPlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchPlaceRepositoryImpl @Inject constructor(
    private val dataSource: SearchPlaceRemoteDataSource
): SearchPlaceRepository {
    override suspend fun getSearchPlace(
        lng: String,
        lat: String,
        place: String
    ): Flow<PlaceInfoList> = flow {
        dataSource.getSearchPlace(lng, lat, place).collect {
            emit(it.toPlaceList())
        }
    }
}