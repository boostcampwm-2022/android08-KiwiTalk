package com.kiwi.kiwitalk.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.repository.ExitChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(private val exitChatRepository: ExitChatRepository) :
    ViewModel() {

    fun exitChat(cid: String) {
        viewModelScope.launch {
            exitChatRepository.exitChat(cid)
        }
    }
}