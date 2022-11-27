package com.kiwi.data.model.remote

import com.google.type.LatLng

data class NewChatRemote(
    val imageUri: String?,
    val chatName: String,
    val chatDescription: String,
    val maxPersonnel: String,
    val keywords: List<String>,
    val address: String,
    val lat: Double,
    val lng: Double
)
