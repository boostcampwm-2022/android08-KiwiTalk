package com.kiwi.kiwitalk.ui.newchat

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.ApiResult
import com.kiwi.domain.PlaceRemote
import com.kiwi.domain.ResultSearchPlace
import com.kiwi.domain.repository.SearchPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPlaceViewModel @Inject constructor(
    private val searchPlaceRepository: SearchPlaceRepository
) : ViewModel() {
    private val _isPlaceList =  MutableLiveData<ResultSearchPlace>()
    val isPlaceList : LiveData<ResultSearchPlace> = _isPlaceList


    fun getSearchPlace(lng:String,lat:String,place: String){
        viewModelScope.launch {
            when(val placeList = searchPlaceRepository.getSearchKeyword(lng,lat,place)){
                is ApiResult.Success -> {
                    _isPlaceList.value = placeList.data
                }
                is ApiResult.Error -> {
                    throw NetworkErrorException("Network 통신 에러")
                }
            }
        }
    }
}