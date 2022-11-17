package com.kiwi.kiwitalk.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        startActivity(Intent(applicationContext, HomeActivity::class.java))
    }
}