package com.kiwi.kiwitalk.ui.keyword.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.kiwitalk.databinding.ItemKeywordBinding

/**
 * TODO innerClass 없애기
 */

class KeywordAdapter(
    val keywordList: MutableList<Keyword>,
    private val keywordClickListener: (View) -> Unit,
    private val selectedKeywordList: List<Keyword>? = null
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

    //TODO 재활용을 위해 일반 클래스로 빼내기
    inner class KeywordViewHolder(val binding: ItemKeywordBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            with(binding.chipKeyword){
                text = keywordList[position].name
                setOnClickListener(keywordClickListener)
                selectedKeywordList?.let { list ->
                    if (list.contains(keywordList[position])) this.isChecked = true
                }
            }
        }
    }
}