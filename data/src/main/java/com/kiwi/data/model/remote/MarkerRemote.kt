package com.kiwi.data.model.remote

data class MarkerRemote(
    val cid: String = "",
    val lat: Double = .0,
    val lng: Double = .0,
    val keywords: List<String> = emptyList()
)
