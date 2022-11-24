package com.kiwi.data.model.remote

data class PlaceRemote (
    val place_name: String, // 장소명, 업체명
    val address_name: String, // 전체 지번 주소
    val road_address_name: String, // 전체 도로명 주소
    val x: String, // X 좌표값 혹은 longitude
    val y: String, // Y 좌표값 혹은 latitude
)