package com.kiwi.kiwitalk.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kiwi.kiwitalk.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout, ChatListFragment()).commit()
    }
}