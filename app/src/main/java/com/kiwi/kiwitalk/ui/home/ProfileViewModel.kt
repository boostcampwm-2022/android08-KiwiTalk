package com.kiwi.kiwitalk.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
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

    init {
        getMyProfile()
    }

    fun getMyProfile() {
        chatClient.getCurrentUser()?.let { user ->
            myName.value = user.name
        }
    }

    fun setUpdateProfile() {
        with(chatClient) {
            getCurrentUser()?.let { user ->
                myName.value?.let { myNameString ->
                    updateUser(user.apply { name = myNameString }).enqueue()
                }
            }
        }
    }
}