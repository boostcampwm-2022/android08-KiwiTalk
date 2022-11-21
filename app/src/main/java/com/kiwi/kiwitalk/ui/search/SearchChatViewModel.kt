package com.kiwi.kiwitalk.ui.search

import androidx.lifecycle.ViewModel
import com.kiwi.domain.repository.SearchChatRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SearchChatViewModel @Inject constructor(
    private val searchChatRepository: SearchChatRepository
) : ViewModel() {

}