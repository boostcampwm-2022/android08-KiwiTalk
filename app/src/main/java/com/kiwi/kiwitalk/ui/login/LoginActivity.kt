package com.kiwi.kiwitalk.ui.login

import android.os.Build
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.kiwi.kiwitalk.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manageSplashScreen: View = findViewById(android.R.id.content)
        manageSplashScreen.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.isReady) {
                        manageSplashScreen.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        binding.btnFakeLogin.setOnClickListener {
            /* 로그인 결과로 받은 idToken 값 넣기 */
            viewModel.signIn(System.currentTimeMillis().toString())
        }

        viewModel.idToken.observe(this) {
            doOnLoginInfoExist(it)
        }

    }

    private fun doOnLoginInfoExist(info: String) {
        /* 액티비티 이동 코드 추가 */
        Snackbar.make(binding.root, "로그인 정보 : $info", Snackbar.LENGTH_SHORT).show()
    }
}