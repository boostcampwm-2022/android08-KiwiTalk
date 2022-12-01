package com.kiwi.kiwitalk.ui.keyword.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.domain.model.keyword.KeywordCategory
import com.kiwi.kiwitalk.databinding.ItemKeywordcategoryBinding

class KeywordCategoryAdapter(
    var keywordCategoryList: List<KeywordCategory>?,
    private val keywordClickListener: (View) -> Unit,
    private val selectedKeywordList: List<Keyword>? = null
) : RecyclerView.Adapter<KeywordCategoryAdapter.KeywordCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordCategoryViewHolder {
        val binding = ItemKeywordcategoryBinding.inflate(LayoutInflater.from(parent.context))
        return KeywordCategoryViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: KeywordCategoryViewHolder, position: Int) {
        keywordCategoryList?.let {
            holder.bind(it[position].keywords, position)
        }
    }

    override fun getItemCount(): Int {
        return if (keywordCategoryList == null) 0 else keywordCategoryList!!.size
    }

    inner class KeywordCategoryViewHolder(
        val binding: ItemKeywordcategoryBinding,
        val contextForLayoutManager: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keywordList: MutableList<Keyword>, position: Int) {
            keywordCategoryList?.let {
                binding.tvKeywordCategoryCategoryName.text = it[position].name
                binding.rvKeywordCategoryKeywordList.layoutManager =
                    FlexboxLayoutManager(contextForLayoutManager).apply {

                    }
                binding.rvKeywordCategoryKeywordList.adapter =
                    KeywordAdapter(
                        it[position].keywords,
                        keywordClickListener,
                        selectedKeywordList
                    )
            }
        }
    }
}