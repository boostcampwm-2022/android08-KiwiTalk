package com.kiwi.kiwitalk.ui.login

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kiwi.kiwitalk.AppPreference
import com.kiwi.kiwitalk.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val pref: AppPreference,
    val googleApiClient: GoogleSignInClient,
    private val connectivityManager: ConnectivityManager,
) : ViewModel() {
    var isNetworkConnect: Boolean = false
    var isReady: Boolean = false

    private val _idToken = MutableLiveData<String>()
    val idToken: LiveData<String> = _idToken

    init {
        viewModelScope.launch {
            isNetworkConnect = checkNetworkState()
            loadLoginHistory()
            isReady = true
        }
    }

    fun signIn(token: String) {
        /* pref에는 token만 넣기 */
        Log.d(LOCATION, "로그인 정보 저장 - token size : ${token.length}")
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
        Log.d("k001", "저장된 토큰 값 : $savedToken")
        /* 기타 토큰 유효성 검사 추가 */
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkNetworkState(): Boolean {
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)

        return if (caps != null) {
            when {
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        } else {
            false
        }
    }

    companion object{
        private const val LOCATION = "LoginViewModel"
    }
}