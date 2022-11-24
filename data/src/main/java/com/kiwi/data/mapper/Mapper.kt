package com.kiwi.data.mapper

import com.kiwi.data.Const
import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import io.getstream.chat.android.client.models.Channel
import com.kiwi.data.mapper.DateFormatter.formatTimeString

object Mapper {
    fun MarkerRemote.toMarker() = Marker(
        cid = cid,
        x = x,
        y = y,
        keywords = keywords
    )

    /*
    * 키워드 경고뜨긴 하는데 데이터만 잘 넘어오면 제대로 작동함.
    * */
    fun Channel.toChatInfo() = ChatInfo(
        cid = this.cid,
        name = this.name,
        keywords = this.extraData["keyword"] as? List<String>? ?: listOf("키워드가 없습니다"),
        description = "채팅방 설명이 없습니다.",
        memberCount = this.memberCount,
        lastMessageAt = this.lastMessageAt?.formatTimeString()?:"오래전",
        country = this.extraData["**주소key**"]?.toString()?: Const.EMPTY_STRING
    )
}