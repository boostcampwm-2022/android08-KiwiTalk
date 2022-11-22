package com.kiwi.domain.model

import java.util.*

data class ChatInfo(
    val cid:String,
    val name: String,
    var memberCount: Int,
    var lastMessageAt: Date?,
)