package com.kiwi.kiwitalk.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Keyword
import com.kiwi.kiwitalk.databinding.ItemChatListNoImageBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter

class ChatAdapter(
    private var chatList: List<ChatInfo>?,
    private val chatClickListener: (ChatInfo) -> Unit
) : RecyclerView.Adapter<ChatInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInfoViewHolder {
        val binding = ItemChatListNoImageBinding.inflate(LayoutInflater.from(parent.context))

        return ChatInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatInfoViewHolder, position: Int) {
        chatList?.let {
            val data = it[position]
            holder.bind(data)
            holder.binding.root.setOnClickListener {
                chatClickListener(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList?.size ?: 0
    }

    fun submitList(list: List<ChatInfo>) {
        chatList = list
        notifyDataSetChanged()
    }
}