package com.kiwi.kiwitalk.ui.keyword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.domain.model.keyword.KeywordCategory
import com.kiwi.domain.repository.SearchKeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchKeywordViewModel @Inject constructor(
    private val searchKeywordRepository: SearchKeywordRepository
): ViewModel() {

    private val _allKeywords = MutableLiveData<List<KeywordCategory>>()
    val allKeywords: LiveData<List<KeywordCategory>>
        get() = _allKeywords


    private val _selectedKeywords = MutableLiveData<List<Keyword>>()
    val selectedKeyword: LiveData<List<Keyword>>
        get() = _selectedKeywords

    private var tempSelectedKeywords: List<Keyword>? = null

    fun getAllKeywords(){
        viewModelScope.launch {
            allKeywords.value ?: searchKeywordRepository.getAllKeyWord()
                .let {
                    _allKeywords.value = it
                    Log.d("FIRESTORE_CALL_KEYWORD", "getKeywords: VM 1 ${it}")
                }
        }
        Log.d("FIRESTORE_CALL_KEYWORD", "getKeywords: VM 2")
    }

    fun setSelectedKeywords(text: String){
        getKeyword(text)?.let { keyword ->
            if (_selectedKeywords.value == null){
                _selectedKeywords.value = listOf(keyword)
            } else {
                val mutableSelectedList = _selectedKeywords.value!!.toMutableList()
                if (mutableSelectedList.contains(keyword)) mutableSelectedList.remove(keyword)
                else mutableSelectedList.add(keyword)
                _selectedKeywords.value = mutableSelectedList
            }
        }
    }

    private fun getKeyword(text: String): Keyword? {
        allKeywords.value?.forEach { keywordCategory ->
            keywordCategory.keywords.forEach { keyword ->
                if (keyword.name == text) return keyword
            }
        }
        return null
    }

    fun saveBeforeKeywords(){
        selectedKeyword.value?.let {
            tempSelectedKeywords = it.toMutableList()
        }
    }

    fun SaveSelectedKeywordOrNot(saveOrNot: Boolean){
        Log.d("SaveSelected", "SaveSelectedKeywordOrNot: ${saveOrNot} before: $tempSelectedKeywords")
        if (!saveOrNot){
                _selectedKeywords.value = tempSelectedKeywords ?: listOf()
        }
        tempSelectedKeywords = null
    }
}