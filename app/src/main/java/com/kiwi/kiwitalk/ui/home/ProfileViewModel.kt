package com.kiwi.kiwitalk.ui.home

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kiwi.domain.model.Keyword
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
    private val _profileImage = MutableLiveData<String?>()
    val profileImage: LiveData<String?> = _profileImage

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
            _profileImage.value = user.image.ifBlank { null }
        }
    }

    fun setMySelectedKeyword(selectKeywordList: List<Keyword>?) {
        selectKeywordList?.let {
            _myKeywords.value = it
        }
    }

    fun setUpdateProfile() {
        chatClient.getCurrentUser()?.let { user ->
            myName.value?.let { myNameString ->
                user.name = myNameString
            }
            myKeywords.value?.let { myKeywordsList ->
                user.extraData.put("keywords", myKeywordsList.map { it.name })
            }
            val uri = profileImage.value
            if (uri == null) {
                updateUser(user, uri)
            } else {
                val ref = Firebase.storage.reference.child("profile/${user.id}")
                ref.putFile(Uri.parse(uri)).addOnSuccessListener {
                    it.storage.downloadUrl.addOnCompleteListener { url ->
                        updateUser(user, url.result.toString())
                    }
                }.addOnFailureListener {
                    Log.d("NewChatDataSource", "putFile Failure: $it")
                }
            }
        }
    }

    private fun updateUser(user: User, uri: String?) {
        user.image = uri ?: ""
        chatClient.updateUser(user).enqueue()
    }

    fun setChatImage(uri: String) {
        _profileImage.value = uri
    }
}