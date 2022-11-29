package com.kiwi.kiwitalk.ui.keyword.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.kiwitalk.databinding.ItemKeywordBinding

class KeywordAdapter(
    val keywordList: MutableList<Keyword>,
    private val keywordClickListener: (View) -> Unit
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


    fun updateList(newData: MutableList<Keyword>) {
        keywordList.clear()
        keywordList.addAll(newData)
        notifyDataSetChanged()
    }

    //TODO 재활용을 위해 일반 클래스로 빼내기
    inner class KeywordViewHolder(val binding: ItemKeywordBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.chipKeyword.text = keywordList[position].name
            binding.chipKeyword.setOnClickListener(keywordClickListener)
        }
    }
}