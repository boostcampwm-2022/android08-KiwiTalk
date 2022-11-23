package com.kiwi.kiwitalk.ui.login

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kiwi.kiwitalk.AppPreference
import com.kiwi.kiwitalk.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val pref: AppPreference,
    val googleApiClient: GoogleSignInClient,
    private val connectivityManager: ConnectivityManager,
    private val chatClient: ChatClient
) : ViewModel() {
    var isNetworkConnect: Boolean = false
    var isReady: Boolean = false

    private val _idToken = MutableLiveData<String>()
    val userId: LiveData<String> = _idToken

    init {
        isNetworkConnect = checkNetworkState()
        loadLoginHistory()
        isReady = true
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


    private fun loadLoginHistory() {
        val savedToken = pref.getString(
            Const.LOGIN_ID_KEY, Const.EMPTY_STRING
        )
        if (savedToken != Const.EMPTY_STRING) {
            _idToken.value = savedToken
            loginWithLocalToken()
        }
        Log.d("k001", "저장된 토큰 값 : $savedToken")
        /* 기타 토큰 유효성 검사 추가 */
    }

    fun saveToken(id: String, name: String, imageUrl: String) {
        /* pref에는 token만 넣기 */
        Log.d(LOCATION, "로그인 정보 저장 - token size : ${id.length}")
        pref.setString(Const.LOGIN_ID_KEY, id)
        pref.setString(Const.LOGIN_NAME_KEY, name)
        pref.setString(Const.LOGIN_URL_KEY, imageUrl)
        _idToken.value = id
    }

    fun loginWithLocalToken() {
        val savedToken = pref.getString(
            Const.LOGIN_ID_KEY, Const.EMPTY_STRING
        )
        if (savedToken != Const.EMPTY_STRING) {
            val user = User(
                id = pref.getString(Const.LOGIN_ID_KEY, Const.EMPTY_STRING),
                name = pref.getString(Const.LOGIN_NAME_KEY, Const.EMPTY_STRING),
                image = pref.getString(Const.LOGIN_URL_KEY, Const.EMPTY_STRING),
            )
            Log.d(LOCATION, "user.id : ${user.id}")
            val token = chatClient.devToken(user.id)
            if (chatClient.getCurrentUser() == null) {
                chatClient.connectUser(user, token).enqueue { result ->
                    Log.d(LOCATION, "result is ${result.isSuccess}: $result")
                }
            } else {
                Log.d(LOCATION, "user: ${chatClient.getCurrentUser()}")
            }
        }
        Log.d("k001", "저장된 토큰 값 : $savedToken")
    }

    companion object{
        private const val LOCATION = "k001"
    }
}