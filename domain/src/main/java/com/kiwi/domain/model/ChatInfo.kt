package com.kiwi.domain.model

data class ChatInfo(
    val cid:String,
    val name: String,
    val keywords: List<String>,
    val description: String,
    val memberCount: Int,
    val lastMessageAt: String,
    val country: String,
)