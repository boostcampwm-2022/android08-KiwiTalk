package com.kiwi.data.mapper

import com.kiwi.data.Const
import com.kiwi.data.mapper.DateFormatter.formatTimeString
import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.data.model.remote.PlaceListRemote
import com.kiwi.data.model.remote.PlaceRemote
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.Place
import com.kiwi.domain.model.PlaceList
import io.getstream.chat.android.client.models.Channel

object Mapper {
    fun MarkerRemote.toMarker() = Marker(
        cid = cid,
        x = x,
        y = y,
        keywords = keywords
    )

    fun PlaceListRemote.toPlaceList(): PlaceList {
        val result = mutableListOf<Place>()
        documents.mapIndexed { index, placeRemote ->
            result.add(index, placeRemote.toPlace())
        }
        return PlaceList(list = result)
    }

    fun PlaceRemote.toPlace() = Place(
        placeName = place_name,
        addressName = address_name,
        roadAddressName = road_address_name,
        lng = x,
        lat = y
    )

    /*
    * 경고가 안 뜨긴 하는데 조금 더러워진 느낌..
    */
    fun Channel.toChatInfo() = ChatInfo(
        cid = this.cid,
        name = this.name,
        keywords = this.extraData["keywords"]?.let {
            if (it is List<*>) it.filterIsInstance<String>() else null
        } ?: listOf("키워드가 없습니다."),
        description = "채팅방 설명이 없습니다.",
        memberCount = this.memberCount,
        lastMessageAt = this.lastMessageAt?.formatTimeString() ?: "오래전",
        country = this.extraData["**주소key**"]?.toString() ?: Const.EMPTY_STRING
    )
}