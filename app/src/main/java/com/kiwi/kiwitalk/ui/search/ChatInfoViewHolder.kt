package com.kiwi.kiwitalk.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Keyword
import com.kiwi.kiwitalk.databinding.ItemChatListNoImageBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter

class ChatInfoViewHolder(
    val binding: ItemChatListNoImageBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(chatInfo: ChatInfo) {
        binding.item = chatInfo
        val adapter = SelectedKeywordAdapter()
        binding.adapter = adapter
        adapter.submitList(chatInfo.keywords.map {
            Keyword(it, 0)
        }.toMutableList())
    }
}