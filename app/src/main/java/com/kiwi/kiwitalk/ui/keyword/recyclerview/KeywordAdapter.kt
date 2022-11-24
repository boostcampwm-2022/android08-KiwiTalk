package com.kiwi.kiwitalk.ui.keyword.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.kiwitalk.databinding.ItemKeywordBinding

class KeywordAdapter(
    val keywordList: MutableList<Keyword>
): RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val binding = ItemKeywordBinding.inflate(LayoutInflater.from(parent.context))
        return KeywordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return keywordList.size
    }

    inner class KeywordViewHolder(val binding: ItemKeywordBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.chipKeyword.text = keywordList[position].name
        }
    }
}