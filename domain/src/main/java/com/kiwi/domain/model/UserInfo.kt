package com.kiwi.domain.model

data class UserInfo(
    val id: String,
    val name: String,
    val keywords : List<Keyword>,
    val imageUrl: String = ""
)
