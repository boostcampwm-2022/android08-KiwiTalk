package com.kiwi.kiwitalk.ui.search

import android.location.Location
import android.util.Log
import androidx.core.location.component1
import androidx.core.location.component2
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.PlaceChatInfo
import com.kiwi.domain.model.Keyword
import com.kiwi.domain.repository.SearchChatRepository
import com.kiwi.data.AppPreference
import com.kiwi.kiwitalk.util.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.utils.toResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchChatMapViewModel @Inject constructor(
    private val searchChatRepository: SearchChatRepository,
    private val chatClient: ChatClient,
    private val preference: AppPreference,
) : ViewModel() {
    private val _markerList = MutableSharedFlow<Marker>()
    val markerList: SharedFlow<Marker> = _markerList

    private val _placeChatInfo = MutableLiveData<PlaceChatInfo>()
    val placeChatInfo: LiveData<PlaceChatInfo> = _placeChatInfo

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private val _clickedChatInfo = MutableLiveData<ChatInfo>()
    val clickedChatInfo: LiveData<ChatInfo> get() = _clickedChatInfo

    fun updateClickedChat(chatInfo: ChatInfo) {
        _clickedChatInfo.value = chatInfo
    }

    fun appendUserToChat(cid: String, userId: String = "") {
        // TODO datasource로 이동시키기
        val targetChannel = chatClient.channel(cid)
        val id = preference.getString(Const.LOGIN_ID_KEY, Const.EMPTY_STRING)
        if (id != Const.EMPTY_STRING) {
            targetChannel.addMembers(listOf(id)).enqueue {
                if (it.isSuccess) {
                    Log.d("k001", "초대 성공")
                } else {
                    Log.d("k001", "초대 실패")
                }
            }
        }

    }

    fun getPlaceInfo(cidList: List<String>) {
        viewModelScope.launch {
            _placeChatInfo.value = searchChatRepository.getPlaceChatList(cidList)
        }
    }

    fun getMarkerList(keywords: List<Keyword>?) {
        if (keywords.isNullOrEmpty()) return
        val (lat, lng) = location.value ?: return
        viewModelScope.launch {
            searchChatRepository.getMarkerList(keywords, lat, lng)
                .catch {
                    Log.d("SearchChatViewModel", "getMarkerList: ${this.toResult()} $it")
                }.collect {
                    Log.d("SearchChatViewModel", "getMarkerList: collect $it")
                    _markerList.emit(it)
                }
        }
    }


    fun setDeviceLocation(newLocation: Location) {
        _location.value = newLocation
    }
}