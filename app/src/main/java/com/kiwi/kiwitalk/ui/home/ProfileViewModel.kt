package com.kiwi.kiwitalk.ui.home

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kiwi.domain.model.Keyword
import com.kiwi.domain.model.UserInfo
import com.kiwi.domain.repository.UserRepository
import com.kiwi.kiwitalk.util.Const
import com.kiwi.kiwitalk.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    //2way binding
    val myName = MutableLiveData<String>()
    val myDescription = MutableLiveData<String>()

    private val _profileImage = MutableLiveData<String?>()
    val profileImage: LiveData<String?> = _profileImage

    private val _myKeywords = MutableLiveData<List<Keyword>>()
    val myKeywords: LiveData<List<Keyword>>
        get() = _myKeywords

    private val _isAllConditionPass = MutableLiveData<Event<Boolean>>()
    val isAllConditionPass: LiveData<Event<Boolean>>
        get() = _isAllConditionPass

    init {
        getMyProfile()
    }

    private fun getMyProfile() {
        userRepository.getUserInfo().let { userInfo ->
            myName.value = userInfo.name
            myDescription.value = userInfo.description
            _myKeywords.value = userInfo.keywords
            _profileImage.value = userInfo.imageUrl.ifBlank { null }
        }
    }

    fun setMySelectedKeyword(selectKeywordList: List<Keyword>?) {
        selectKeywordList?.let {
            _myKeywords.value = it
        }
    }

    fun setUpdateProfile() {
        val uri = profileImage.value
        val id = userRepository.getUserInfo().id
        if (uri == null || uri.contains("https://")) {
            updateUser(
                UserInfo(
                    id = id,
                    name = myName.value ?: Const.EMPTY_STRING,
                    keywords = myKeywords.value ?: listOf(),
                    imageUrl = uri ?: Const.EMPTY_STRING,
                    description = myDescription.value ?: Const.EMPTY_STRING
                )
            )
        } else {
            val ref = Firebase.storage.reference.child("profile/${id}")
            ref.putFile(Uri.parse(uri)).addOnSuccessListener {
                it.storage.downloadUrl.addOnCompleteListener { url ->
                    updateUser(
                        UserInfo(
                            id = id,
                            name = myName.value ?: Const.EMPTY_STRING,
                            keywords = myKeywords.value ?: listOf(),
                            imageUrl = url.result.toString(),
                            description = myDescription.value ?: Const.EMPTY_STRING
                        )
                    )
                }
            }.addOnFailureListener {
                Log.d("NewChatDataSource", "putFile Failure: ${it.cause}")
            }
        }
    }

    private fun updateUser(userInfo: UserInfo) {
        userRepository.updateUser(userInfo)
    }

    fun setChatImage(uri: String) {
        _profileImage.value = uri
    }

    fun checkInput() {
        _isAllConditionPass.value = Event(checkName())
    }

    private fun checkName(): Boolean {
        val nameStrng = myName.value
        nameStrng?.let {
            return !nameStrng.contains(Regex("[^가-힣a-zA-Z]"))
        }
        return false
    }
}