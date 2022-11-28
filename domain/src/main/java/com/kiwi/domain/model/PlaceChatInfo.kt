package com.kiwi.domain.model

data class PlaceChatInfo(
    val chatList: List<ChatInfo>,
) {
    fun getChattingNumber(): Int {
        return chatList.size
    }

    fun getPopularChat(): ChatInfo {
        return chatList.first()
    }

    fun getPlaceInfo(): String {
        return chatList.first().country
    }
}