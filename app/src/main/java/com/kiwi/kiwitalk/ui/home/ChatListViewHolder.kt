package com.kiwi.kiwitalk.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kiwi.kiwitalk.databinding.ChatListItemBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.ui.ChatUI
import io.getstream.chat.android.ui.channel.list.ChannelListView
import io.getstream.chat.android.ui.channel.list.adapter.ChannelListPayloadDiff
import io.getstream.chat.android.ui.channel.list.adapter.viewholder.BaseChannelListItemViewHolder
import io.getstream.chat.android.ui.channel.list.adapter.viewholder.ChannelListItemViewHolderFactory

class ChatListItemViewHolderFactory : ChannelListItemViewHolderFactory() {
    override fun createChannelViewHolder(parentView: ViewGroup): BaseChannelListItemViewHolder {
        return ChatListViewHolder(parentView, listenerContainer.channelClickListener)
    }
}

class ChatListViewHolder(
    parent: ViewGroup,
    private val channelClickListener: ChannelListView.ChannelClickListener,
    private val binding: ChatListItemBinding = ChatListItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : BaseChannelListItemViewHolder(binding.root) {
    private lateinit var channel: Channel

    init {
        binding.root.setOnClickListener { channelClickListener.onClick(channel) }
    }

    override fun bind(channel: Channel, diff: ChannelListPayloadDiff) {
        this.channel = channel

        binding.apply {
            avatarView.setChannelData(channel)
            tvChatTitle.text = ChatUI.channelNameFormatter.formatChannelName(
                channel, ChatClient.instance().getCurrentUser()
            )
            tvLocation.text = (channel.extraData["location"] ?: "location").toString()
            tvTags.text = (channel.extraData["tags"] as? List<*>)?.joinToString(" ") ?: "태그"
        }
    }
}