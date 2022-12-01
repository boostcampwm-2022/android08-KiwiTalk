package com.kiwi.data.model.remote

data class NewChatRemote(
    val imageUri: String = "",
    val chatName: String,
    val chatDescription: String,
    val maxMemberCnt: String,
    val keywords: List<String>,
    val address: String,
    val lat: Double,
    val lng: Double
)
