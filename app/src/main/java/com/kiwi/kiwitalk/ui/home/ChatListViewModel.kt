package com.kiwi.kiwitalk.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.repository.ExitChatRepository
import com.kiwi.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val exitChatRepository: ExitChatRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val TAG = "K001|ChatListViewModel"

    private val _signOutState = MutableLiveData<Boolean>()
    val signOutState: LiveData<Boolean>
        get() = _signOutState

    fun exitChat(cid: String) {
        viewModelScope.launch {
            exitChatRepository.exitChat(cid)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut().catch {
            }.collect {
                _signOutState.postValue(it)
            }
        }
    }
}