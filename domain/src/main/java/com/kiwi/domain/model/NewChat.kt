package com.kiwi.domain.model

data class NewChat(
    val imageUri: String = "",
    val chatName: String,
    val chatDescription: String,
    val maxPersonnel: String,
    val keywords: List<String>,
    val address: String,
    val lat: Double,
    val lng: Double
)
