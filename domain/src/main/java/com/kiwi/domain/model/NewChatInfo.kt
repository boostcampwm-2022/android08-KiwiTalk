package com.kiwi.domain.model

data class NewChatInfo(
    val imageUri: String = "",
    val chatName: String,
    val chatDescription: String,
    val maxMemberCnt: String,
    val keywords: List<String>,
    val address: String,
    val lat: Double,
    val lng: Double
)
