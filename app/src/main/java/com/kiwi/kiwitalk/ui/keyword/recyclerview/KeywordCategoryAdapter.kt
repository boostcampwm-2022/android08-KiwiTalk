package com.kiwi.kiwitalk.ui.keyword.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout.DividerMode
import com.google.android.flexbox.FlexboxLayoutManager
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.domain.model.keyword.KeywordCategory
import com.kiwi.kiwitalk.databinding.ItemKeywordcategoryBinding

class KeywordCategoryAdapter(
    private val keywordCategoryList: List<KeywordCategory>
): RecyclerView.Adapter<KeywordCategoryAdapter.KeywordCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordCategoryViewHolder {
        val binding = ItemKeywordcategoryBinding.inflate(LayoutInflater.from(parent.context))
        return KeywordCategoryViewHolder(binding,parent.context)
    }

    override fun onBindViewHolder(holder: KeywordCategoryViewHolder, position: Int) {
        holder.bind(keywordCategoryList[position].keywords,position)
    }

    override fun getItemCount(): Int {
        return keywordCategoryList.size
    }

    inner class KeywordCategoryViewHolder(
        val binding: ItemKeywordcategoryBinding,
        val contextForLayoutManager: Context,
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(keywordList: MutableList<Keyword>, position: Int){
            binding.tvKeywordCategoryCategoryName.text = keywordCategoryList[position].name
            binding.rvKeywordCategoryKeywordList.layoutManager =
                FlexboxLayoutManager(contextForLayoutManager).apply {

                }
            binding.rvKeywordCategoryKeywordList.adapter = KeywordAdapter(keywordCategoryList[position].keywords)
        }
    }
}