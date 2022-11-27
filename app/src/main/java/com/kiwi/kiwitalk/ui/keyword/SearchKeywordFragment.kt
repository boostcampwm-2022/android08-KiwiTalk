package com.kiwi.kiwitalk.ui.keyword

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.kiwi.kiwitalk.databinding.FragmentSearchKeywordBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.KeywordCategoryAdapter
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchKeywordFragment : Fragment() {

    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels()
    private lateinit var binding: FragmentSearchKeywordBinding
    private lateinit var keywordCategoryAdapter: KeywordCategoryAdapter
    private lateinit var selectedKeywordAdapter: SelectedKeywordAdapter
    private val keywordClickListener: (View) -> Unit = { chip ->
        Log.d("KEYWORD_CLICK", "clicked!: ")
        searchKeywordViewModel.setSelectedKeywords((chip as Chip).text.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchKeywordBinding.inflate( inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewmodel = searchKeywordViewModel

        setAdapter()
        setObserve()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchKeywordViewModel.getAllKeywords()
    }

    private fun setObserve(){
        searchKeywordViewModel.allKeywords.observe(viewLifecycleOwner){ list ->
            keywordCategoryAdapter.keywordCategoryList = list
            keywordCategoryAdapter.notifyDataSetChanged()
        }

        searchKeywordViewModel.selectedKeyword.observe(viewLifecycleOwner){ list ->
            selectedKeywordAdapter.submitList(list)
        }
    }

    private fun setAdapter(){
        keywordCategoryAdapter = KeywordCategoryAdapter(searchKeywordViewModel.allKeywords.value,keywordClickListener)
        binding.rvSearchKeywordKeywordCategoryList.adapter = keywordCategoryAdapter

        selectedKeywordAdapter = SelectedKeywordAdapter(keywordClickListener)
        binding.rvSearchKeywordSelectedKeywordList.adapter = selectedKeywordAdapter
    }
}