package com.kiwi.data.mapper

import com.kiwi.chatmapper.ChatKey
import com.kiwi.chatmapper.ChatMapper
import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.data.model.remote.NewChatRemote
import com.kiwi.data.model.remote.PlaceListRemote
import com.kiwi.data.model.remote.PlaceRemote
import com.kiwi.domain.model.*
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User

object Mapper {
    fun MarkerRemote.toMarker() = Marker(
        cid = cid,
        x = lat,
        y = lng,
        keywords = keywords
    )

    fun PlaceListRemote.toPlaceList(): PlaceInfoList {
        val result = mutableListOf<PlaceInfo>()
        documents.mapIndexed { index, placeRemote ->
            result.add(index, placeRemote.toPlace())
        }
        return PlaceInfoList(list = result)
    }

    fun PlaceRemote.toPlace() = PlaceInfo(
        placeName = place_name,
        addressName = address_name,
        roadAddressName = road_address_name,
        lng = x,
        lat = y
    )

    fun Channel.toChatInfo() = ChatInfo(
        cid = this.cid,
        name = this.name,
        keywords = ChatMapper.getKeywords(this.extraData),
        description = ChatMapper.getDescription(this.extraData),
        memberCount = this.memberCount,
        lastMessageAt = ChatMapper.getDateOfLastMassage(this.lastMessageAt),
        address = ChatMapper.getTrimAddress(this.extraData),
    )

    fun NewChatInfo.toNewChatRemote() = NewChatRemote(
        imageUri = this.imageUri,
        chatName = this.chatName,
        chatDescription = this.chatDescription,
        maxMemberCount = this.maxMemberCnt,
        keywords = this.keywords,
        address = this.address,
        lat = this.lat,
        lng = this.lng
    )


    fun User.toUserInfo(): UserInfo {
        val description = this.extraData["description"] as? String? ?: ""
        return UserInfo(
            id = this.id,
            name = this.name,
            keywords = ChatMapper.getKeywords(extraData).map { Keyword(it) },
            imageUrl = image,
            description = ChatMapper.getDescription(extraData)
        )
    }

    fun UserInfo.toUser(): User {
        val user = User(
            id = this.id,
            name = this.name,
            image = this.imageUrl
        )
        user.extraData[ChatKey.CHAT_KEYWORDS] = this.keywords.map { it.name }
        user.extraData[ChatKey.CHAT_DESCRIPTION] = this.description
        return user
    }
}