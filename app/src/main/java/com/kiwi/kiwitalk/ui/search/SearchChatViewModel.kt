package com.kiwi.kiwitalk.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.model.Marker
import com.kiwi.domain.repository.SearchChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.utils.toResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchChatViewModel @Inject constructor(
    private val searchChatRepository: SearchChatRepository
) : ViewModel() {
    private val _markerList = MutableSharedFlow<Marker>()
    val markerList: SharedFlow<Marker> = _markerList

    fun getMarkerList(keywords: List<String>, x: Double, y: Double) {
        viewModelScope.launch {
            searchChatRepository.getMarkerList(keywords, x, y)
                .catch {
                    Log.d("searchChatRepository", "getMarkerList: ${this.toResult()} $it")
                }.collect {
                    _markerList.emit(it)
                }
        }
    }
}