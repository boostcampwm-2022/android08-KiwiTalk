package com.kiwi.kiwitalk.ui.newchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.kiwitalk.databinding.ItemChatListNoImageBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter

class ChatAdapter(
    var chatList: List<ChatInfo>?,
    private val clickListener: (View) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInfoViewHolder {
        val binding = ItemChatListNoImageBinding.inflate(LayoutInflater.from(parent.context))
        binding.root.setOnClickListener(clickListener)
        return ChatInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatInfoViewHolder, position: Int) {
        chatList?.let {
            holder.bind(it[position], position)
        }
    }

    override fun getItemCount(): Int {
        return chatList?.size ?: 0
    }

    fun submitList(list: List<ChatInfo>) {
        chatList = list
        notifyDataSetChanged()
    }

    inner class ChatInfoViewHolder(
        val binding: ItemChatListNoImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatInfo: ChatInfo, position: Int) {
            binding.item = chatInfo
            val adapter = SelectedKeywordAdapter()
            binding.adapter = adapter
            adapter.submitList(chatInfo.keywords.map {
                Keyword(it, 0)
            }.toMutableList())
        }
    }

}