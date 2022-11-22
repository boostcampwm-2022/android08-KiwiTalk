package com.kiwi.data.mapper

import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import io.getstream.chat.android.client.models.Channel

object Mapper {
    fun MarkerRemote.toMarker() = Marker(
        cid = cid,
        x = x,
        y = y,
        keywords = keywords
    )

    fun Channel.toChatInfo() = ChatInfo(
        cid = this.cid,
        name = this.name,
        memberCount = this.memberCount,
        lastMessageAt = this.lastMessageAt,
    )
}