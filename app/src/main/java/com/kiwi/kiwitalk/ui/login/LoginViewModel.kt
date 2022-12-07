package com.kiwi.kiwitalk.ui.login

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kiwi.data.AppPreference
import com.kiwi.kiwitalk.util.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
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

    private val _loginState = MutableLiveData<Boolean>(false)
    val loginState: LiveData<Boolean> = _loginState

    init {
        isNetworkConnect = checkNetworkState()
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

    fun saveToken(id: String, name: String, imageUrl: String) {
        /* pref에는 token만 넣기 */
        Log.d(LOCATION, "로그인 정보 저장 - token size : ${id.length}")
        pref.setString(Const.LOGIN_ID_KEY, id)
        pref.setString(Const.LOGIN_NAME_KEY, name)
        pref.setString(Const.LOGIN_URL_KEY, imageUrl)
    }

    fun loginWithLocalToken() {
        val savedToken = pref.getString(
            Const.LOGIN_ID_KEY, Const.EMPTY_STRING
        )

        if (isValidToken(savedToken)) {
            val user = User(
                id = pref.getString(Const.LOGIN_ID_KEY, Const.EMPTY_STRING),
                name = pref.getString(Const.LOGIN_NAME_KEY, Const.EMPTY_STRING),
                image = pref.getString(Const.LOGIN_URL_KEY, Const.EMPTY_STRING),
            )
            Log.d(LOCATION, "user.id : ${user.id}")
            val token = chatClient.devToken(user.id)
            if (chatClient.getCurrentUser() == null) {
                chatClient.connectUser(user, token).enqueue { result ->
                    _loginState.value = result.isSuccess
                }
            } else {
                Log.d(LOCATION, "user: ${chatClient.getCurrentUser()}")
                _loginState.value = true
            }
        } else {
            pref.setString(Const.LOGIN_ID_KEY, Const.EMPTY_STRING)
            _loginState.value = false
        }
    }

    private fun isValidToken(token: String): Boolean {
        return tokenRegex.matches(token)
    }

    companion object {
        private const val LOCATION = "k001"
        private val tokenRegex = Regex("[0-9,a-z]{1,21}")
    }
}