package com.kiwi.kiwitalk.ui.keyword.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.kiwitalk.databinding.ItemKeywordBinding

class SelectedKeywordAdapter(val keywordClickListener: (View) -> Unit): ListAdapter<Keyword,SelectedKeywordAdapter.SelectedKeywordViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedKeywordViewHolder {
        val binding = ItemKeywordBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SelectedKeywordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedKeywordViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class SelectedKeywordViewHolder(val binding: ItemKeywordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: Keyword){
            with(binding.chipKeyword){
                text = keyword.name
                isChecked = true
                isClickable = false
//                setOnClickListener(keywordClickListener)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Keyword>() {
            override fun areItemsTheSame(
                oldItem: Keyword,
                newItem: Keyword
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Keyword,
                newItem: Keyword
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


