package com.kiwi.kiwitalk.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kiwi.domain.UserUiCallback
import com.kiwi.kiwitalk.NetworkStateManager
import com.kiwi.kiwitalk.databinding.ActivitySplashBinding
import com.kiwi.kiwitalk.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding
    private val networkStateManager by lazy { NetworkStateManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        networkStateManager.register()
        viewModel.loginWithLocalToken(object : UserUiCallback {
            override fun onSuccess() {
                navigateToHomeActivity()
            }

            override fun onFailure(e: Throwable) {
                Log.d("K001|Splash", e.toString())
                navigateToLoginActivity()
            }
        })
    }

    private fun navigateToLoginActivity() {
        finishAffinity()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun navigateToHomeActivity() {
        finishAffinity()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun onStop() {
        super.onStop()
        networkStateManager.unregister()
    }

}