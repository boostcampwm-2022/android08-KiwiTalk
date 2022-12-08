package com.kiwi.kiwitalk.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kiwi.domain.UserUiCallback
import com.kiwi.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    val googleApiClient: GoogleSignInClient,
    private val userRepository: UserRepository
) : ViewModel() {
    var isReady: Boolean = false

    private val _loginState = MutableLiveData<Boolean>(false)
    val loginState: LiveData<Boolean> = _loginState

    init {
        //loginWithLocalToken()
        isReady = true
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

//    fun loginWithLocalToken() {
//        userRepository.isRemoteLoginRequired(object : UserUiCallback {
//            override fun onSuccess() {
//                _loginState.value = true
//            }
//            override fun onFailure(e: Throwable) {
//                Log.d(TAG, "Login fail")
//                e.printStackTrace()
//            }
//        })
//    }

    companion object {
        private const val TAG = "k001|LoginVM"
    }
}