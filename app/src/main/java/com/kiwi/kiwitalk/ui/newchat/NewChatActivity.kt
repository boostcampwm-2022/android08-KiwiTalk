package com.kiwi.kiwitalk.ui.newchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.ActivityNewChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewChatBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}