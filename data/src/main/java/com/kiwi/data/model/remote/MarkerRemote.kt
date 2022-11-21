package com.kiwi.data.model.remote

data class MarkerRemote(
    val cid: String = "",
    val x: Double = .0,
    val y: Double = .0,
    val keywords: List<String> = emptyList()
)
