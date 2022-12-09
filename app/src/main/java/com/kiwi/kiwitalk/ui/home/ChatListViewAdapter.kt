package com.kiwi.kiwitalk.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.domain.model.Keyword
import com.kiwi.kiwitalk.databinding.ItemChatListBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter
import com.kiwi.kiwitalk.util.Const
import io.getstream.chat.android.client.models.Channel

class ChatListViewAdapter(private val onClickListener: OnChatClickListener) :
    ListAdapter<Channel, RecyclerView.ViewHolder>(ChatDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder -> {
                holder.bind(getItem(position))
                holder.itemView.setOnClickListener {
                    onClickListener.onChatClick(getItem(position))
                }
                holder.itemView.setOnLongClickListener {
                    onClickListener.onChatLongClick(getItem(position))
                    true
                }
            }
        }

    }

    class ChatViewHolder(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Channel) {
            binding.chat = item
            val keywordAdapter = SelectedKeywordAdapter()
            val keywords = item.extraData[Const.CHAT_KEYWORDS] as? List<String>
            keywordAdapter.submitList(keywords?.map { Keyword(it, 0) } ?: emptyList<Keyword>())
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