package com.kiwi.domain.model

data class PlaceChatInfo (
    val chatList: List<ChatInfo>,
    val placeInfo: String,
){
    fun getChattingNumber(): Int{
        return chatList.size
    }

    fun getPopularChat(): ChatInfo?{
        return chatList.minByOrNull { it -> it.cid }!!
    }
}