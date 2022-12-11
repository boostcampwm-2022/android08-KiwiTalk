package com.kiwi.kiwitalk.ui.login

import androidx.lifecycle.ViewModel
import com.kiwi.domain.UserUiCallback
import com.kiwi.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
): ViewModel() {

    fun loginWithLocalToken(userUiCallback: UserUiCallback){
        userRepository.isRemoteLoginRequired(userUiCallback)
    }

    companion object {
        private const val TAG = "k001|SplashVM"
    }
}