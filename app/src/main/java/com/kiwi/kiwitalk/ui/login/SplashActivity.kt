package com.kiwi.kiwitalk.ui.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kiwi.kiwitalk.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    /**
     * 해야하는 일
     * 1.네트워크 체크
     * 2. 로컬에 저장된 정보 있는지 확인
     * 3. 로컬 정보가 stream에 등록되어있는지 확인
     * 3-1. if yes -> 바로 HomeActivity로 보냄
     * 3-2 else -> 토큰 지우고 LoginActivity로 보냄
     * */
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

        }
    }

}