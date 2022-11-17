package com.kiwi.kiwitalk.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwi.kiwitalk.AppPreference
import com.kiwi.kiwitalk.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val pref: AppPreference,
) : ViewModel() {
    var isReady: Boolean = false
    private val _idToken = MutableLiveData<String>()
    val idToken: LiveData<String> = _idToken

    init {
        viewModelScope.launch {
            loadLoginHistory()
            isReady = true
        }
    }

    fun signIn(token: String) {
        /* pref에는 token만 넣기 */
        pref.setString(Const.LOGIN_HISTORY_KEY, token)
        _idToken.value = token
    }

    private fun loadLoginHistory() {
        val savedToken = pref.getString(
            Const.LOGIN_HISTORY_KEY, Const.EMPTY_STRING
        )
        if (savedToken != Const.EMPTY_STRING) {
            _idToken.value = savedToken
        }
        /* 기타 토큰 유효성 검사 추가 */
    }

    private fun checkNetworkState() {
        // 구현 예정
    }
}