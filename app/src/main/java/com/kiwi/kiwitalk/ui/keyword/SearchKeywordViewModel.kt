package com.kiwi.kiwitalk.ui.keyword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.model.keyword.KeywordCategory
import com.kiwi.domain.repository.SearchKeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchKeywordViewModel @Inject constructor(
    private val searchKeywordRepository: SearchKeywordRepository
): ViewModel() {

    private val _keywords = MutableLiveData<List<KeywordCategory>>()
    val keywords: LiveData<List<KeywordCategory>>
        get() = _keywords


    fun getAllKeywords(){
        viewModelScope.launch {
            searchKeywordRepository.getAllKeyWord().also { Log.d("FIRESTORE_CALL_KEYWORD", "getKeywords: VM 1 ${it}") }
                .let {
                    _keywords.value = it
                }
        }
        Log.d("FIRESTORE_CALL_KEYWORD", "getKeywords: VM 2")
    }

}