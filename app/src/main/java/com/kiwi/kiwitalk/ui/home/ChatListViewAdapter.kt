package com.kiwi.kiwitalk.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiwi.kiwitalk.databinding.ChatListItemBinding
import io.getstream.chat.android.client.models.Channel

class ChatListViewAdapter : ListAdapter<Channel, RecyclerView.ViewHolder>(ChatListDiffUtil) {
    lateinit var onClickListener: OnChatListClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatListViewHolder(
            ChatListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatListViewHolder -> {
                holder.bind(getItem(position))
                holder.itemView.setOnClickListener {
                    onClickListener.onChatListClick(getItem(position))
                }
            }
        }

    }

    class ChatListViewHolder(private val binding: ChatListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Channel) {
            Log.d("ChatListViewAdapter", "bind: ${item.image}")
            binding.chatList = item
        }
    }

    interface OnChatListClickListener {
        fun onChatListClick(channel: Channel)
    }

    companion object {
        val ChatListDiffUtil = object : DiffUtil.ItemCallback<Channel>() {
            override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
                return oldItem.cid == newItem.cid
            }

            override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
                return oldItem == newItem
            }
        }
    }
}