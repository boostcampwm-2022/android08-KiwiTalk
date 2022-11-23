package com.kiwi.kiwitalk.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.PlaceChatInfo
import com.kiwi.domain.repository.SearchChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchChatViewModel @Inject constructor(
    private val searchChatRepository: SearchChatRepository
) : ViewModel() {

    private val _placeChatInfo = MutableLiveData<PlaceChatInfo>()
    val placeChatInfo : LiveData<PlaceChatInfo> = _placeChatInfo

    fun getPlaceInfo(marker: Marker){
        viewModelScope.launch {
            _placeChatInfo.value = searchChatRepository.getChat(marker)
        }
    }
}