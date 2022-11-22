package com.kiwi.kiwitalk.ui.search

import androidx.lifecycle.ViewModel
import com.kiwi.domain.repository.SearchChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchChatViewModel @Inject constructor(
    private val searchChatRepository: SearchChatRepository
) : ViewModel() {

}