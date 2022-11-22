package com.kiwi.kiwitalk.ui.keyword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentSearchKeywordBinding

class SearchKeywordFragment : Fragment() {

    private lateinit var binding: FragmentSearchKeywordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchKeywordBinding.inflate( inflater,container,false)

        return binding.root
    }
}