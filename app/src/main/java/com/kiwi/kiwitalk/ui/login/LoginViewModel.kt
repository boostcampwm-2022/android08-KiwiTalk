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
import com.kiwi.data.AppPreference
import com.kiwi.domain.UserUiCallback
import com.kiwi.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val pref: AppPreference,
    val googleApiClient: GoogleSignInClient,
    private val connectivityManager: ConnectivityManager,
    private val chatClient: ChatClient,
    private val userRepository: UserRepository
) : ViewModel() {
    var isNetworkConnect: Boolean = false
    var isReady: Boolean = false

    private val _loginState = MutableLiveData<Boolean>(false)
    val loginState: LiveData<Boolean> = _loginState

    init {
        isNetworkConnect = checkNetworkState()
        loginWithLocalToken()
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

    fun signUp(id: String, name: String, imageUrl: String) {
        viewModelScope.launch {
            userRepository.tryLogin(id, name, imageUrl, object : UserUiCallback {
                override fun onSuccess() {
                    _loginState.value = true
                }
                override fun onFailure(e: Throwable) {
                    Log.d(TAG, "Login fail")
                    e.printStackTrace()
                }
            })
        }
    }

    fun loginWithLocalToken() {
        userRepository.isRemoteLoginRequired(object : UserUiCallback {
            override fun onSuccess() {
                _loginState.value = true
            }
            override fun onFailure(e: Throwable) {
                Log.d(TAG, "Login fail")
                e.printStackTrace()
            }
        })
    }

    companion object {
        private const val TAG = "k001|LoginVM"
    }
}