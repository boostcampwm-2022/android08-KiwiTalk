package com.kiwi.kiwitalk.ui.newchat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.model.PlaceList
import com.kiwi.domain.repository.SearchPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.utils.toResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPlaceViewModel @Inject constructor(
    private val searchPlaceRepository: SearchPlaceRepository
) : ViewModel() {

    private val _isPlaceList = MutableSharedFlow<PlaceList>()
    val isPlaceList: SharedFlow<PlaceList> = _isPlaceList

    fun getSearchPlace(lng: String, lat: String, place: String) {
        viewModelScope.launch {
            searchPlaceRepository.getSearchKeyword(lng, lat, place)
                .catch {
                    Log.d("getSearchPlace","getSearchPlace: ${this.toResult()} $it")
                }.collect {
                    _isPlaceList.emit(it)
                }
        }
    }
}

