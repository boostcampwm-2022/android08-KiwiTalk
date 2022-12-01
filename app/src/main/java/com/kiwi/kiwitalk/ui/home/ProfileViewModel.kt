package com.kiwi.kiwitalk.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kiwi.domain.model.keyword.Keyword
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import javax.inject.Inject


/**
 * 너무나 단순한 작업에서 MVVM구조를 따라야할까?
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    val chatClient: ChatClient
) : ViewModel() {

    //2way binding
    val myName = MutableLiveData<String>()

    private val _myKeywords = MutableLiveData<List<Keyword>>()
    val myKeywords: LiveData<List<Keyword>>
        get() = _myKeywords

    init {
        getMyProfile()
    }

    fun getMyProfile() {
        chatClient.getCurrentUser()?.let { user ->
            myName.value = user.name
            user.extraData.get("keywords")?.let { keywordStringList ->
                _myKeywords.value = (keywordStringList as List<*>).map { keywordString ->
                    Keyword(keywordString as String)
                }
            }
        }
    }

    fun setMySelectedKeyword(selectKeywordList: List<Keyword>?) {
        selectKeywordList?.let {
            _myKeywords.value = it
        }
    }

    fun setUpdateProfile() {
        with(chatClient) {
            getCurrentUser()?.let { user ->
                myName.value?.let { myNameString ->
                    user.name = myNameString
                }
                myKeywords.value?.let { myKeywordsList ->
                    user.extraData.put("keywords", myKeywordsList.map { it.name })
                }
                updateUser(user).enqueue()
            }
        }
    }
}