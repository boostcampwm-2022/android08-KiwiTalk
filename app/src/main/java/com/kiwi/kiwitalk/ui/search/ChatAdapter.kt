package com.kiwi.kiwitalk.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.kiwitalk.databinding.ItemChatListNoImageBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter

class ChatAdapter(
    var chatList: List<ChatInfo>?,
    private val chatClickListener: (ChatInfo) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInfoViewHolder {
        val binding = ItemChatListNoImageBinding.inflate(LayoutInflater.from(parent.context))
        //binding.root.setOnClickListener(clickListener)
        return ChatInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatInfoViewHolder, position: Int) {
        chatList?.let {
            val data = it[position]
            holder.bind(data)
            // cid를 받아올 수 있는 방법이 기억 안나서 create 말고 bind에 넣음
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

    inner class ChatInfoViewHolder(
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

}