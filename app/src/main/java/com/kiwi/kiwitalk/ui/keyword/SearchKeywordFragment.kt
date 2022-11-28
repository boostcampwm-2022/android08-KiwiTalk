package com.kiwi.kiwitalk.ui.keyword

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kiwi.kiwitalk.databinding.FragmentSearchKeywordBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.KeywordCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchKeywordFragment : Fragment() {

    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels()

    private lateinit var binding: FragmentSearchKeywordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchKeywordBinding.inflate( inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewmodel = searchKeywordViewModel


        setObserve()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchKeywordViewModel.getAllKeywords()

    }

    fun setObserve(){
        searchKeywordViewModel.keywords.observe(viewLifecycleOwner){
            binding.rvSearchKeywordKeywordCategoryList.adapter = KeywordCategoryAdapter(it)
        }
    }

}