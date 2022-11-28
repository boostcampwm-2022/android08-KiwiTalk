package com.kiwi.kiwitalk.ui.newchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kiwi.domain.model.NewChat
import com.kiwi.domain.repository.NewChatRepository
import com.kiwi.kiwitalk.AppPreference
import com.kiwi.kiwitalk.Const.EMPTY_STRING
import com.kiwi.kiwitalk.Const.LOGIN_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewChatViewModel @Inject constructor(
    private val repository: NewChatRepository,
    private val pref: AppPreference
) : ViewModel() {

    private val _isChatImage = MutableLiveData<String>()
    val isChatImage: LiveData<String> = _isChatImage

    private val _isChatId = MutableLiveData<String>()
    val isChatId: LiveData<String> = _isChatId

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

    fun setChatId(){
        _isChatId.value = pref.getString(LOGIN_ID_KEY, EMPTY_STRING)
    }

    fun addNewChat(userid: String, currentTime: String, newChat: NewChat) {
        viewModelScope.launch {
            repository.addFireBaseChat(userid + currentTime,newChat)
            repository.addStreamChat(userid,currentTime,newChat)
        }
    }
}

