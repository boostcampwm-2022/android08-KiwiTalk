package com.kiwi.kiwitalk.ui.login

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.kiwi.kiwitalk.databinding.DialogLoginProgressBinding

class ProgressDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogLoginProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}