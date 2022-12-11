package com.kiwi.data.datasource.remote

import com.kiwi.data.model.remote.PlaceListRemote
import kotlinx.coroutines.flow.Flow

interface SearchPlaceRemoteDataSource {
    suspend fun getSearchPlace(lng:String,lat:String,place: String): Flow<PlaceListRemote>
}