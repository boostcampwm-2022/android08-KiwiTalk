package com.kiwi.data.datasource.remote

import android.accounts.NetworkErrorException
import com.kiwi.data.model.remote.PlaceListRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject

class SearchPlaceRemoteDataSourceImpl @Inject constructor(
    client: Retrofit?
) : SearchPlaceRemoteDataSource {

    private val searchService = client?.create(SearchService::class.java)

    override suspend fun getSearchPlace(
        lng: String,
        lat: String,
        place: String
    ): Flow<PlaceListRemote> = flow {
        val response = searchService?.getSearchKeyword(lng,lat,"15000",place)
        val body = response?.body()
        if(response?.isSuccessful == true && body != null){
            emit(body)
        } else {
            throw NetworkErrorException("Network Connect Error")
        }
    }
}
