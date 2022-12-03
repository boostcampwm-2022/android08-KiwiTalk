package com.kiwi.domain.repository

import com.kiwi.domain.model.PlaceInfoList
import kotlinx.coroutines.flow.Flow

interface SearchPlaceRepository {
   suspend fun getSearchKeyword(lng:String,lat:String,place: String): Flow<PlaceInfoList>
}