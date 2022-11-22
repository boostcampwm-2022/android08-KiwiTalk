package com.kiwi.kiwitalk.ui.keyword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.repository.SearchKeywordRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchKeywordViewModel @Inject constructor(
    val searchKeywordRepository: SearchKeywordRepository
): ViewModel() {
    private val _keywords = MutableLiveData<String>()
    val keywords: LiveData<String>
        get() = _keywords

    fun getKeywords(){
        viewModelScope.launch {
            _keywords.value = searchKeywordRepository.getAllKeyWord()
        }
    }

}