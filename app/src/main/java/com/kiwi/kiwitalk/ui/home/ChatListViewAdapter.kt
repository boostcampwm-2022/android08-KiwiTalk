package com.kiwi.kiwitalk.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.chatmapper.ChatMapper
import com.kiwi.domain.model.Keyword
import com.kiwi.kiwitalk.databinding.ItemChatListBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter
import io.getstream.chat.android.client.models.Channel

class ChatListViewAdapter(private val onClickListener: OnChatClickListener) :
    ListAdapter<Channel, RecyclerView.ViewHolder>(ChatDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemChatListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val holder = ChatViewHolder(binding)
        binding.root.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onClickListener.onChatClick(getItem(position))
            }
        }
        binding.root.setOnLongClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onClickListener.onChatLongClick(getItem(position))
            }
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder -> holder.bind(getItem(position))
        }
    }

    class ChatViewHolder(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Channel) {
            binding.chat = item
            val keywordAdapter = SelectedKeywordAdapter()
            val keywords = ChatMapper.getKeywords(item.extraData)
            keywordAdapter.submitList(keywords.map { Keyword(it, 0) })
            binding.tvRvChatListKeyword.adapter = keywordAdapter
        }
    }

    interface OnChatClickListener {
        fun onChatClick(channel: Channel)

        fun onChatLongClick(channel: Channel)
    }

    companion object {
        val ChatDiffUtil = object : DiffUtil.ItemCallback<Channel>() {
            override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
                return oldItem.cid == newItem.cid
            }

            override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
                return oldItem.cid == newItem.cid && oldItem.updatedAt == newItem.updatedAt &&
                        (oldItem.unreadCount == newItem.unreadCount || newItem.unreadCount == null || oldItem.unreadCount == null) &&
                        oldItem.memberCount == newItem.memberCount
            }
        }
    }
}