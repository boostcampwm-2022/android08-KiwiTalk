package com.kiwi.kiwitalk.ui.newchat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kiwi.domain.model.NewChat
import com.kiwi.domain.model.PlaceList
import com.kiwi.domain.repository.NewChatRepository
import com.kiwi.domain.repository.SearchPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.utils.toResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewChatViewModel @Inject constructor(
    private val repository: NewChatRepository
) : ViewModel() {

    private val _isChatImage = MutableLiveData<String>()
    val isChatImage: LiveData<String> = _isChatImage

    private val _isNewChatInfo = MutableLiveData<NewChat>()
    val isNewChatInfo: LiveData<NewChat> = _isNewChatInfo

    private val _isAddress = MutableLiveData<String>()
    val isAddress: LiveData<String> = _isAddress

    private val _isLatLng = MutableLiveData<LatLng>()
    val isLatLng: LiveData<LatLng> = _isLatLng


    fun setChatImage(uri: String) {
        _isChatImage.value = uri
    }

    fun setAddress(address: String) {
        _isAddress.value = address
    }

    fun setLatLng(latLng: LatLng) {
        _isLatLng.value = latLng
    }

    fun setNewChat(newChat: NewChat) {
        _isNewChatInfo.value = newChat
    }

    fun addNewChat(newChat: NewChat) {
        viewModelScope.launch {
            repository.addFireBaseChat(newChat)
        }
    }
}

